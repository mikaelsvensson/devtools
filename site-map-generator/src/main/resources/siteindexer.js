(function() {

    var $ = jQuery;

    SiteIndexer = function (rootFolder, index, name) {
		this.resultElement = null;
		this.resultContainerElement = null;
		this.rootFolder = rootFolder;
		this.index = index;
		this.selectedIndex = -1;
		this.name = name;
		this.lastResult = null;
    }

	SiteIndexer.prototype.KEY_ENTER    =13;
	SiteIndexer.prototype.KEY_LEFT     =37;
    SiteIndexer.prototype.KEY_UP       =38;
    SiteIndexer.prototype.KEY_RIGHT    =39;
    SiteIndexer.prototype.KEY_DOWN     =40;
	
    SiteIndexer.prototype.init = function () {
		var name = this.name;

		this.resultElement = $(document.createElement("div")).addClass("siteindexer-result");
		this.resultContainerElement = $(document.createElement("div")).addClass("siteindexer-resultwrapper");
		this.resultContainerElement.mousedown(this, this.onResultMouseDown);
		this.resultContainerElement.click(this, this.onResultClick);
		
		this.resultContainerElement.append(this.resultElement);
		$(document.body).append(this.resultContainerElement);

		var field = $("#" + name + "-searchfield");
		field.focus(this, this.onSearchFieldFocus);
		field.blur(this, this.onSearchFieldBlur);
		field.keyup(this, this.onSearchFieldKeyUp);
    }
	
	SiteIndexer.prototype.onResultMouseDown = function (e) {
		clearTimeout(this.pendingResultClose);
	};
	
	SiteIndexer.prototype.onResultClick = function (e) {
        var indexer = e.data;
        indexer.setSelectedIndex($(e.target).index());
		indexer.acceptSelection();
	};
	
	SiteIndexer.prototype.onSearchFieldFocus = function (e) {
        var indexer = e.data;

        indexer.repositionResultElement();
        indexer.toggleResult(true);
	};

	SiteIndexer.prototype.repositionResultElement = function () {
        var xy = $("#" + this.name + "-searchfield").offset();
        var h = $("#" + this.name + "-field").height();
        this.resultContainerElement.css("top", xy.top + h).css("left", xy.left);
	};

	SiteIndexer.prototype.onSearchFieldBlur = function (e) {
        var indexer = e.data;
        // Delay the closing of the results to prevent the results from been hidden before
        // the browser has time to fire the onClick event, in case the onBlur is triggered by
        // a mouse click on one of the result items.
		indexer.delayedResultClose();
	};

	SiteIndexer.prototype.delayedResultClose = function () {
		var indexer = this;
		this.pendingResultClose = setTimeout( function () {
			indexer.toggleResult(false);
		}, 100);
	};
	
	SiteIndexer.prototype.onSearchFieldKeyUp = function (e) {
        var indexer = e.data;
        if (e.keyCode == indexer.KEY_DOWN) {
            indexer.moveSelectionDown();
        } else if (e.keyCode == indexer.KEY_UP) {
            indexer.moveSelectionUp();
        } else if (e.keyCode == indexer.KEY_ENTER) {
            indexer.acceptSelection();
        } else {
            var query = this.value;
            indexer.performSearch(query);
        }
	};

	SiteIndexer.prototype.setSelectedIndex = function (index) {
        if (this.selectedIndex != -1) {
            this.resultElement.children().eq(0).children().eq(this.selectedIndex).removeClass("selected");
        }
        
        this.selectedIndex = index;
        
        if (this.selectedIndex != -1) {
            this.resultElement.children().eq(0).children().eq(this.selectedIndex).addClass("selected");
        }
	};
	
	SiteIndexer.prototype.moveSelectionUp = function () {
		this.setSelectedIndex(this.selectedIndex - 1 < 0 ? this.lastResult.length - 1 : this.selectedIndex - 1);
	};
	
	SiteIndexer.prototype.moveSelectionDown = function () {
		this.setSelectedIndex(this.selectedIndex + 1 == this.lastResult.length ? 0 : this.selectedIndex + 1);
	};
	
	SiteIndexer.prototype.acceptSelection = function () {
		location.href = this.lastResult[this.selectedIndex].link;
		this.toggleResult(false);
	};
	
    SiteIndexer.prototype.performSearch = function (query) {
		var result = this.search(query);
		result = result.sort(function (a, b) { return b.points - a.points; });
		var html = [];
		html.push("<ul>");
		for (var i in result) {
			var title = result[i].title;
			var points = result[i].points;
			html.push("<li>" + title + " (" + points + " p)</li>");
		}
		html.push("</ul>");
		
		this.toggleResult(result.length > 0);
		
		this.resultElement.get(0).innerHTML = html.join("");

		this.setSelectedIndex(result.length > 0 ? 0 : -1);
		
		this.lastResult = result;
	};

    SiteIndexer.prototype.toggleResult = function (show) {
		this.resultContainerElement.toggleClass("show", show && this.lastResult != null && this.lastResult.length > 0);
	};
	
    SiteIndexer.prototype.search = function (query) {
		var result = [];
		if (query.length > 0) {
			var tempResult = {};
			for (var i in this.index) {
				var moduleIndex = this.index[i];
				var entries = moduleIndex.data;
				for (var x in entries) {
				    var entry = entries[x];
                    for (var word in entry.wordCount) {
                        var count = entry.wordCount[word];

                        if (word.indexOf(query) != -1) {
                            var uri = (moduleIndex.baseUrl ? moduleIndex.baseUrl : this.rootFolder) + entry.uri;
                            if (!tempResult[uri]) {
                                tempResult[uri] = {
                                        points: count,
                                        title: entry.title ? entry.title : entry.uri
                                        };
                            } else {
                                tempResult[uri].points += count ;
                            }
                        }
                    }
				}
			}
			for (var uri in tempResult) {
				result.push({ 
						"link": uri,
						"title": tempResult[uri].title,
						"points": tempResult[uri].points
						});
			}
		}
		return result;
    }
} ());
