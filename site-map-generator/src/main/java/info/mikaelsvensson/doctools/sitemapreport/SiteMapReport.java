package info.mikaelsvensson.doctools.sitemapreport;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @goal sitemap
 * @phase site
 */
public class SiteMapReport extends AbstractMavenReport {
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

        File root = new File(getOutputDirectory());
        new FolderSiteMapGenerator(getSink(), getString(locale, "sitemap.outputfoldertree.title")).printFolderContent(root);
    }

    /**
     * @param siteRenderer
     *         The siteRenderer to set.
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
