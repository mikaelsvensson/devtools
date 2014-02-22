package info.mikaelsvensson.devtools.report.sitemap;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

public class SiteMapFolderEntry extends SiteMapEntry {
    private Collection<SiteMapEntry> children;

    public SiteMapFolderEntry(final File folder) {
        super(folder);
        children = new LinkedList<SiteMapEntry>();
    }

    public Collection<SiteMapEntry> getFiles() {
        Collection<SiteMapEntry> files = new LinkedList<SiteMapEntry>();
        for (SiteMapEntry child : children) {
            if (!(child instanceof SiteMapFolderEntry)) {
                files.add(child);
            }
        }
        return files;
    }

    public Collection<SiteMapFolderEntry> getFolders() {
        Collection<SiteMapFolderEntry> folders = new LinkedList<SiteMapFolderEntry>();
        for (SiteMapEntry child : children) {
            if (child instanceof SiteMapFolderEntry) {
                folders.add((SiteMapFolderEntry) child);
            }
        }
        return folders;
    }

    public boolean addChild(final SiteMapEntry siteMapEntry) {
        return children.add(siteMapEntry);
    }

}
