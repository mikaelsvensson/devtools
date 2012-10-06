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
		
		this.resultContainerElement.append(this.resultElement);
//		$("#" + name).append(this.resultContainerElement);
		$(document.body).append(this.resultContainerElement);

		var field = $("#" + name + "-searchfield");
		field.focus(this, function(e) {
			var indexer = e.data;

			var xy = $(this).offset();
			var h = $("#" + name + "-field").height();
			indexer.resultContainerElement.css("top", xy.top + h).css("left", xy.left);

			indexer.toggleResult(true);
		});
		field.blur(this, function(e) {
			var indexer = e.data;
			indexer.toggleResult(false);
		});
		field.keyup(this, function(e) {
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
		});
		console.log("OK");
    }
	
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
	};
	
    SiteIndexer.prototype.performSearch = function (query) {
		var result = this.search(query);
		result = result.sort(function (a, b) { return b.points - a.points; });
		var html = [];
		html.push("<ul>");
		for (var i in result) {
			var uri = result[i].link;
			var points = result[i].points;
			html.push("<li>" + uri + " (" + points + ")</li>");
		}
		html.push("</ul>");
		
		this.toggleResult(result.length > 0);
		
		this.resultElement.html(html.join(""));
		
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
				var entry = this.index[i];
				for (var word in entry.wordCount) {
					var count = entry.wordCount[word];
					if (word.indexOf(query) != -1) {
						if (!tempResult[entry.uri]) {
							tempResult[entry.uri] = { points: count };
						} else {
							tempResult[entry.uri].points += count ;
						}
					}
				}
			}
			for (var uri in tempResult) {
				result.push({ "link": this.rootFolder + uri, "points": tempResult[uri].points });
			}
		}
		return result;
    }
} ());
