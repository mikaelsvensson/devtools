package info.mikaelsvensson.devtools.sitesearch;

import org.apache.commons.collections.Predicate;

public interface WordEntryFilter extends Predicate {
    boolean accept(WordCount entry);
}
