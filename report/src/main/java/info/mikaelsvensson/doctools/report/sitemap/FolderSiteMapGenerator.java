package info.mikaelsvensson.doctools.report.sitemap;

import info.mikaelsvensson.doctools.report.HtmlFileCreator;

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
        fileCreator.printHeading1(title);

        printFolderContent(getRoot(folder), folder.toURI());
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
                fileCreator.printListItemLink(pageStrategy.getTitle(), "../" + href.substring(0, href.lastIndexOf('.')) + ".html");
            }
        }
        fileCreator.listEnd();
    }
}
