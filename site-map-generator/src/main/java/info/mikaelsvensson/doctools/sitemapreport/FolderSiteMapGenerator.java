package info.mikaelsvensson.doctools.sitemapreport;

import org.apache.maven.doxia.sink.Sink;

import java.io.File;
import java.net.URI;

public class FolderSiteMapGenerator implements SiteMapGenerator {
    private Sink sink;
    private String title;

    public FolderSiteMapGenerator(final Sink sink, final String title) {
        this.sink = sink;
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
        printHead(title);

        sink.body();

        printHeading1(title);

        printFolderContent(getRoot(folder), folder.toURI());

        sink.body_();

        sink.flush();
        sink.close();
    }
    public void printFolderContent(SiteMapFolderEntry folder, final URI rootURI) {
        sink.list();
        for (SiteMapFolderEntry folderEntry : folder.getFolders()) {
            sink.listItem();
            sink.text(folderEntry.getFile().getName() + ":");
            printFolderContent(folderEntry, rootURI);
            sink.listItem_();
        }
        for (SiteMapEntry fileEntry : folder.getFiles()) {

            PageStrategy pageStrategy = PageStrategyFactory.getInstance().createPageStrategy(fileEntry.getFile());
            if (pageStrategy != null) {
                sink.listItem();

                String href = rootURI.relativize(fileEntry.getFile().toURI()).toString();
                sink.link("../" + href.substring(0, href.lastIndexOf('.')) + ".html");
                sink.text(pageStrategy.getTitle());
                sink.link_();
                sink.listItem_();
            }
        }
        sink.list_();
    }
    private void printHeading1(String text) {
        sink.sectionTitle1();
        sink.text(text);
        sink.sectionTitle1_();
    }

    private void printHeading2(String text) {
        sink.sectionTitle2();
        sink.text(text);
        sink.sectionTitle2_();
    }

    private void printParagraph(String text) {
        sink.paragraph();
        sink.text(text);
        sink.paragraph();
    }

    private void printHead(String title) {
        sink.head();
        sink.title();
        sink.text(title);
        sink.title_();
        sink.head_();
    }
}
