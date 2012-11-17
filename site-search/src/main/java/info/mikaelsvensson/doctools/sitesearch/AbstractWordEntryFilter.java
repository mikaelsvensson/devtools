package info.mikaelsvensson.doctools.sitesearch;

import org.apache.commons.collections.Predicate;

public abstract class AbstractWordEntryFilter implements WordEntryFilter, Predicate {
    @Override
    public boolean evaluate(final Object object) {
        return object instanceof WordCount && accept((WordCount) object);
    }

}
