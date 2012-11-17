package info.mikaelsvensson.doctools.report;

import java.io.File;

public interface SiteMapGenerator {
    SiteMapFolderEntry getRoot(File folder);
}
