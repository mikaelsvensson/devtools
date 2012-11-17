package info.mikaelsvensson.doctools.sitemapreport;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class DoctoolsReport extends AbstractMavenReport implements HtmlFileCreatorFactory {

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
    protected Renderer siteRenderer;
    /**
     * Directory where reports will go.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     * @readonly
     */
    private String outputDirectory;

    private Collection<HtmlFileCreator> htmlFileCreators;
    @Override
    protected void executeReport(final Locale locale) throws MavenReportException {
        htmlFileCreators = new LinkedList<HtmlFileCreator>();

        render(
                createHtmlFileCreator(getDefaultPageTitle(locale), getSink()),
                this,
                locale);

        for (HtmlFileCreator htmlFileCreator : htmlFileCreators) {
            htmlFileCreator.close();
        }
    }

    protected abstract void render(HtmlFileCreator defaultPageCreator, HtmlFileCreatorFactory pageCreatorFactory, final Locale locale);

    protected abstract String getDefaultPageTitle(final Locale locale);

    @Override
    public HtmlFileCreator createNewHtmlPage(final String fileName, final String documentTitle) throws IOException {
        String outputName = getOutputName();
        String subFolder = "";
        int pos = outputName.lastIndexOf('/');
        if (pos >= 0) {
            subFolder = outputName.substring(0, pos + 1);
        }
        Sink sink = getSinkFactory().createSink(new File(getOutputDirectory()), subFolder + File.separatorChar + fileName);
        return createHtmlFileCreator(documentTitle, sink);
    }

    private HtmlFileCreator createHtmlFileCreator(final String documentTitle, final Sink sink) {
        HtmlFileCreator creator = new HtmlFileCreator(sink, documentTitle);
        htmlFileCreators.add(creator);
        return creator;
    }

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

    protected String getString(Locale locale, String key) {
        return getBundle(locale).getString(key);
    }

    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(getClass().getName(), locale, getClass().getClassLoader());
    }
}
