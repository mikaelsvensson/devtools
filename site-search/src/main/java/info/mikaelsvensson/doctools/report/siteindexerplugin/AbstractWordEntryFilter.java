package info.mikaelsvensson.doctools.report.siteindexerplugin;

import org.apache.commons.collections.Predicate;

public abstract class AbstractWordEntryFilter implements WordEntryFilter, Predicate {
    @Override
    public boolean evaluate(final Object object) {
        if (object instanceof WordCount) {
            return accept((WordCount)object);
        }
        return false;
    }

}
