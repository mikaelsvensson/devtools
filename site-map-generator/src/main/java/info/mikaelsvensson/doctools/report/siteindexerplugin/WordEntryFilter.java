package info.mikaelsvensson.doctools.report.siteindexerplugin;

import java.util.Map;

public interface WordEntryFilter {
    boolean accept(Map.Entry<String, Integer> entry);
}
