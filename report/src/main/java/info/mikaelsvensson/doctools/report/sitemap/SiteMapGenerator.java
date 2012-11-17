package info.mikaelsvensson.doctools.report.sitemap;

import java.io.File;

public interface SiteMapGenerator {
    SiteMapFolderEntry getRoot(File folder);
}
