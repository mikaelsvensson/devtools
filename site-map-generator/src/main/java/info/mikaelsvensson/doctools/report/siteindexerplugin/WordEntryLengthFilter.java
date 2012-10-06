package info.mikaelsvensson.doctools.report.siteindexerplugin;

class WordEntryLengthFilter extends AbstractWordEntryFilter {

    private int limit;

    WordEntryLengthFilter(final int limit) {
        this.limit = limit;
    }

    @Override
    public boolean accept(final WordCount entry) {
        return entry.getWord().length() >= limit;
    }
}
