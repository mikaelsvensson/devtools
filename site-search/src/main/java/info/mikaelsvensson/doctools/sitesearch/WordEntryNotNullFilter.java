package info.mikaelsvensson.devtools.sitesearch;

public class WordEntryNotNullFilter extends AbstractWordEntryFilter {

    @Override
    public boolean accept(final WordCount entry) {
        return entry.getWord() != null && !"null".equals(entry.getWord());
    }
}
