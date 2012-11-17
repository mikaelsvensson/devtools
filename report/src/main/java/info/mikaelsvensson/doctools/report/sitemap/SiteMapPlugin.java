package info.mikaelsvensson.doctools.report.sitemap;

import org.apache.maven.doxia.sink.render.RenderingContext;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.site.decoration.io.xpp3.DecorationXpp3Reader;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.doxia.siterenderer.RendererException;
import org.apache.maven.doxia.siterenderer.SiteRenderingContext;
import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;
import org.apache.maven.doxia.tools.SiteTool;
import org.apache.maven.doxia.tools.SiteToolException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @goal sitemap-standalone
 * @phase site
 */
public class SiteMapPlugin extends AbstractMojo {
    /**
     * Directory where reports will go.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     */
    private String buildDirectoryPath;
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
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     * @readonly
     */
    private String sourceDirectory;
    /**
     * SiteTool.
     *
     * @component
     */
    private SiteTool siteTool;
    private DecorationModel defaultDecorationModel;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File buildFolder = new File(buildDirectoryPath);
        File siteFolder = new File(buildFolder, "site");
        getLog().info("siteFolder = " + siteFolder.toURI().toString());
//        RenderingContext context = new RenderingContext(siteFolder, "sitemap-standalone.html");
//        SiteRendererSink sink = new SiteRendererSink(context);


        Writer writer = null;
        try {

            String filename = "sitemap-standalone.html";

            Locale locale = Locale.getDefault();

            SiteRenderingContext siteContext = new SiteRenderingContext();
            DecorationModel decorationModel = getDefaultDecorationModel();//getDecorationModel();
            siteContext.setDecoration(decorationModel);
            siteContext.setTemplateName("org/apache/maven/doxia/siterenderer/resources/default-site.vm");
            siteContext.setLocale(locale);

            Map pluginContext = getPluginContext();

            RenderingContext context = new RenderingContext(siteFolder, filename);

            SiteRendererSink sink = new SiteRendererSink(context);

            new FolderSiteMapGenerator(sink, "SITE MAP TITLE").printFolderContent(siteFolder);

            siteFolder.mkdirs();

            writer = new OutputStreamWriter(new FileOutputStream(new File(siteFolder, filename)), "UTF-8");

            siteRenderer.generateDocument(writer, sink, siteContext);
        } catch (RendererException e) {
            throw new MojoExecutionException("An error has occurred in site map generation.", e);
        } catch (IOException e) {
            throw new MojoExecutionException("An error has occurred in site map generation.", e);
//        } catch (XmlPullParserException e) {
//            throw new MojoExecutionException("An error has occurred in site map generation.", e);
        } finally {
            IOUtil.close(writer);
        }
    }

    private DecorationModel getDecorationModel() throws IOException, XmlPullParserException {
        DecorationXpp3Reader decorationXpp3Reader = new DecorationXpp3Reader();
        return decorationXpp3Reader.read(new FileInputStream(sourceDirectory + "/../../site/site.xml"));
    }

    private DecorationModel getDefaultDecorationModel()
            throws MojoExecutionException {
        if (this.defaultDecorationModel == null) {
            final Locale locale = Locale.getDefault();

            final File basedir = project.getBasedir();
            final String relativePath = siteTool.getRelativePath(new File(sourceDirectory + "/../../site").getAbsolutePath(), basedir.getAbsolutePath());

            final File descriptorFile = siteTool.getSiteDescriptorFromBasedir(relativePath, basedir, locale);
            DecorationModel decoration = null;

            if (descriptorFile.exists()) {
                XmlStreamReader reader = null;
                try {
                    reader = ReaderFactory.newXmlReader(descriptorFile);
                    String enc = reader.getEncoding();

                    String siteDescriptorContent = IOUtil.toString(reader);
                    siteDescriptorContent =
                            siteTool.getInterpolatedSiteDescriptorContent(new HashMap(), project,
                                    siteDescriptorContent, enc, enc);

                    decoration = new DecorationXpp3Reader().read(new StringReader(siteDescriptorContent));
                } catch (XmlPullParserException e) {
                    throw new MojoExecutionException("Error parsing site descriptor", e);
                } catch (IOException e) {
                    throw new MojoExecutionException("Error reading site descriptor", e);
                } catch (SiteToolException e) {
                    throw new MojoExecutionException("Error when interpoling site descriptor", e);
                } finally {
                    IOUtil.close(reader);
                }
            }

            this.defaultDecorationModel = decoration;
        }

        return this.defaultDecorationModel;
    }
}
