package info.mikaelsvensson.doctools.sitemapreport;

import java.io.File;

public interface SiteMapGenerator {
    SiteMapFolderEntry getRoot(File folder);
}
