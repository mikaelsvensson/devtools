package info.mikaelsvensson.doctools.report.sitesearch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IndexEntry {
    private String uri;
    private String title;
    private Map<String, Integer> wordCount;

    public IndexEntry(final String title, final String uri, final Collection<WordCount> words) {
        this.title = title;
        this.uri = uri;
        this.wordCount = new HashMap<String, Integer>();
        for (WordCount word : words) {
            wordCount.put(word.getWord(), word.getCount());
        }
    }

    public String getTitle() {
        return title;
    }

/*
    public void setTitle(final String title) {
        this.title = title;
    }
*/

    public String getUri() {
        return uri;
    }

/*
    public void setUri(final String uri) {
        this.uri = uri;
    }
*/

    public Map<String, Integer> getWordCount() {
        return wordCount;
    }

/*
    public void setWordCount(final Collection<WordCount> wordCount) {
        this.wordCount = wordCount;
    }
*/
}
