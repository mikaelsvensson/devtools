/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
