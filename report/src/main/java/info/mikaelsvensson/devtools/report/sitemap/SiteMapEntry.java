package info.mikaelsvensson.devtools.report.sitemap;

import java.io.File;

public class SiteMapEntry {
    private File file;

    public SiteMapEntry(final File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }
}
