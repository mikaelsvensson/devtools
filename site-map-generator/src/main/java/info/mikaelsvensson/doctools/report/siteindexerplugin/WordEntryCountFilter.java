package info.mikaelsvensson.doctools.report.siteindexerplugin;

class WordEntryCountFilter extends AbstractWordEntryFilter {

    private int limit;

    WordEntryCountFilter(final int limit) {
        this.limit = limit;
    }

    @Override
    public boolean accept(final WordCount entry) {
        return entry.getCount() > limit;
    }
}
