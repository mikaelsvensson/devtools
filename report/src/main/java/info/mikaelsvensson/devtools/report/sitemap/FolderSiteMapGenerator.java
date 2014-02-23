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

import info.mikaelsvensson.devtools.report.HtmlFileCreator;

import java.io.File;
import java.net.URI;

public class FolderSiteMapGenerator implements SiteMapGenerator {
    private HtmlFileCreator fileCreator;
    private String title;

    public FolderSiteMapGenerator(final HtmlFileCreator fileCreator, final String title) {
        this.fileCreator = fileCreator;
        this.title = title;
    }

    @Override
    public SiteMapFolderEntry getRoot(File folder) {
        return createFolderEntry(folder);
    }

    private SiteMapFolderEntry createFolderEntry(File folder) {
        SiteMapFolderEntry folderEntry = new SiteMapFolderEntry(folder);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    folderEntry.addChild(createFolderEntry(file));
                } else {
                    folderEntry.addChild(new SiteMapEntry(file));
                }
            }
        }
        return folderEntry;
    }
    public void printFolderContent(File folder) {
        fileCreator.printSectionStart(1, title);

        printFolderContent(getRoot(folder), folder.toURI());

        fileCreator.printSectionEnd(1);
    }
    public void printFolderContent(SiteMapFolderEntry folder, final URI rootURI) {
        fileCreator.listStart();
        for (SiteMapFolderEntry folderEntry : folder.getFolders()) {
            fileCreator.listItemStart();
            fileCreator.printText(folderEntry.getFile().getName() + ":");
            printFolderContent(folderEntry, rootURI);
            fileCreator.listItemEnd();
        }
        for (SiteMapEntry fileEntry : folder.getFiles()) {

            PageStrategy pageStrategy = PageStrategyFactory.getInstance().createPageStrategy(fileEntry.getFile());
            if (pageStrategy != null) {
                String href = rootURI.relativize(fileEntry.getFile().toURI()).toString();
                fileCreator.printLinkListItem(pageStrategy.getTitle(), "../" + href.substring(0, href.lastIndexOf('.')) + ".html");
            }
        }
        fileCreator.listEnd();
    }
}
