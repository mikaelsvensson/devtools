package info.mikaelsvensson.doctools.sitemapreport;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.net.URI;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @goal sitemap
 * @phase site
 */
public class SiteMap extends AbstractMavenReport {
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    /**
     * <i>Maven Internal</i>: The Doxia Site Renderer.
     *
     * @component
     */
    private Renderer siteRenderer;
    /**
     * Directory where reports will go.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     * @readonly
     */
    private String outputDirectory;

    /**
     * Directory where reports will go.
     *
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     * @readonly
     */
    private String sourceDirectory;

    @Override
    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    @Override
    protected String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    protected MavenProject getProject() {
        return project;
    }

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        String title = getName(locale);

        Sink sink = getSink();

        printHead(title);

        sink.body();

        printHeading1(title);

        printParagraph(title);

        printSiteMapTree();

        sink.body_();

        sink.flush();
        sink.close();
    }

    private void printSiteMapTree() {
        File folder = new File(new File(sourceDirectory).getParentFile().getParentFile(), "site");

        printFolderContent(getSink(), new File(folder, "apt"));
    }

    private void printHeading1(String text) {
        Sink sink = getSink();
        sink.sectionTitle1();
        sink.text(text);
        sink.sectionTitle1_();
    }

    private void printParagraph(String text) {
        Sink sink = getSink();
        sink.paragraph();
        sink.text(text);
        sink.paragraph();
    }

    private void printHead(String title) {
        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text(title);
        sink.title_();
        sink.head_();
    }

    private void printFolderContent(Sink sink, File folder) {
        printFolderContent(sink, folder, folder.toURI());
    }

    private void printFolderContent(Sink sink, File folder, URI rootURI) {
        File[] files = folder.listFiles();
        if (files != null) {
            sink.list();
            for (File file : files) {
                if (file.isDirectory()) {
                    sink.listItem();
                    sink.text(file.getName() + ":");
                    printFolderContent(sink, file, rootURI);
                    sink.listItem_();
                } else {
                    PageStrategy pageStrategy = PageStrategyFactory.getInstance().createPageStrategy(file);
                    if (pageStrategy != null) {
                        sink.listItem();

                        String href = rootURI.relativize(file.toURI()).toString();
                        sink.link("../" + href.substring(0, href.lastIndexOf('.')) + ".html");
                        sink.text(pageStrategy.getTitle());
                        sink.link_();
                        sink.listItem_();
                    }
                }
            }
            sink.list_();
        }
    }

    /**
     * @param siteRenderer The siteRenderer to set.
     */
    public void setSiteRenderer(Renderer siteRenderer) {
        this.siteRenderer = siteRenderer;
    }

    @Override
    public String getOutputName() {
        return "site-map/index";
    }

    @Override
    public String getName(Locale locale) {
        return getString(locale, "sitemap.title");
    }

    private String getString(Locale locale, String key) {
        return getBundle(locale).getString(key);
    }

    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(getClass().getName(), locale, getClass().getClassLoader());
    }

    @Override
    public String getDescription(Locale locale) {
        return getString(locale, "sitemap.description");
    }
}
