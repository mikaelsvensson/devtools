package info.mikaelsvensson.doctools.report.xslt;

import info.mikaelsvensson.doctools.common.PathUtils;
import info.mikaelsvensson.doctools.report.DoctoolsReport;
import info.mikaelsvensson.doctools.report.HtmlFileCreator;
import info.mikaelsvensson.doctools.report.HtmlFileCreatorFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/** @goal xsltreport */
public class XSLTReport extends DoctoolsReport {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private static final String SIMPLE_REPORT_NAME = "xslt-report";
    /** @parameter expression="${xsltreport.name}" */
    private String name;

    /** @parameter expression="${xsltreport.description}" */
    private String description;

    /** @parameter expression="${xsltreport.reports}" */
    private Report[] reports;

    @Override
    protected void render(final HtmlFileCreator defaultPageCreator, final HtmlFileCreatorFactory pageCreatorFactory, final Locale locale) {
        Map<File, String> reportTitles = renderReports(pageCreatorFactory, defaultPageCreator.getFile());

        renderIndexPage(defaultPageCreator, reportTitles, getName(locale), getDescription(locale));
    }

    private Map<File, String> renderReports(final HtmlFileCreatorFactory pageCreatorFactory, final File indexPageFile) {
        Map<File, String> reportTitles = new TreeMap<File, String>();
        for (int i = 0, reportsLength = reports.length; i < reportsLength; i++) {
            Report report = reports[i];
            if (StringUtils.isEmpty(report.getName())) {
                report.setName("report-" + i);
            }
            getLog().info("Rendering report '" + report.getName() + "'.");
            try {
                File tempDir = createTempDir();
                File tempDefaultFile = new File(tempDir, report.getOutputFile());
                writeFile(new File(report.getXmlFile()), tempDefaultFile, new File(report.getXslFile()), new HashMap<String, String>());

                File reportFolder = getReportFolder(report);
                reportFolder.mkdirs();

                File defaultFile = new File(reportFolder, report.getOutputFile());
                String title = getTitle(tempDefaultFile, report.getName());
                reportTitles.put(defaultFile, title);

                createAndWrapReportPages(pageCreatorFactory, tempDir, reportFolder, indexPageFile);

                FileUtils.deleteDirectory(tempDir);
            } catch (TransformerException e) {
                getLog().warn(e);
            } catch (ParserConfigurationException e) {
                getLog().warn(e);
            } catch (IOException e) {
                getLog().warn(e);
            } catch (SAXException e) {
                getLog().warn(e);
            }
        }
        return reportTitles;
    }

    private void renderIndexPage(final HtmlFileCreator pageCreator, final Map<File, String> links, final String header, final String body) {
        pageCreator.printSectionStart(1, header);
        if (StringUtils.isNotEmpty(body)) {
            pageCreator.printParagraph(body);
        }
        pageCreator.listStart(SIMPLE_REPORT_NAME + "-list");
        for (Map.Entry<File, String> entry : links.entrySet()) {
            pageCreator.printLinkListItem(entry.getValue(), PathUtils.getRelativePath(pageCreator.getFile(), entry.getKey()));

        }
        pageCreator.listEnd();
        pageCreator.printSectionEnd(1);
    }

    private void createAndWrapReportPages(final HtmlFileCreatorFactory pageCreatorFactory, final File sourceFolder, final File targetFolder, final File indexPageFile) throws IOException {
        Collection<File> allFiles = FileUtils.listFiles(sourceFolder, new String[]{"html", "htm"}, true);
        for (File file : allFiles) {
            String fileName = file.getAbsolutePath().substring(sourceFolder.getAbsolutePath().length() + 1);
            File mergedFile = new File(targetFolder, fileName);
            HtmlFileCreator htmlFileCreator = pageCreatorFactory.createNewHtmlPage(mergedFile, getTitle(file, "Title"));
            htmlFileCreator.printRaw(FileUtils.readFileToString(file));
            htmlFileCreator.printLinkParagraph("Back", PathUtils.getRelativePath(mergedFile, indexPageFile));
        }
    }

    private String getTitle(final File file, final String defaultTitle) {
        try {
            org.jsoup.nodes.Document document = Jsoup.parse(file, "UTF-8", "http://invalid.host");
            for (String tagName : new String[]{"h1", "h2", "h3"}) {
                Elements headerTags = document.getElementsByTag(tagName);
                if (headerTags.size() > 0) {
                    return headerTags.get(0).text();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return defaultTitle;
    }

    private File getReportFolder(Report report) {
        return new File(getOutputDirectory(), report.getName());
    }

    private File createTempDir() {
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        File outputDir = new File(tmpDirPath, SIMPLE_REPORT_NAME + "-" + UUID.randomUUID().toString());
        outputDir.mkdir();
        return outputDir;
    }

    @Override
    protected String getDefaultPageTitle(final Locale locale) {
        return "Report List";
    }

    @Override
    public String getOutputName() {
        // TODO: It seems necessary to put the index file in a sub folder since the actual reports are put into sub folders, otherwise "the sink" will not calculate relative paths to CSS and image files correctly. There is probably a good solution for this (minor) issue.
        return SIMPLE_REPORT_NAME + "/index";
    }

    @Override
    public String getName(final Locale locale) {
        return StringUtils.isEmpty(name) ? "XSLT Report" : name;
    }

    @Override
    public String getDescription(final Locale locale) {
        return StringUtils.isEmpty(description) ? "" : description;
    }

    public static void writeFile(Document doc, File file, File xsltFile, Map<String, String> parameters) throws TransformerException {
        Source xsltSource = new StreamSource(xsltFile);
        writeFile(doc, file, xsltSource, parameters);
    }

    public static void writeFile(File input, File output, File xsltFile, Map<String, String> parameters) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        Document doc = documentBuilder.parse(input);
        writeFile(doc, output, xsltFile, parameters);
    }

    private static void writeFile(Document input, File output, Source xsltSource, Map<String, String> parameters) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);
        StreamResult outputTarget = new StreamResult(output);

        Source xmlSource = new DOMSource(input);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue());
        }

        transformer.setParameter("outputFile", output.getName());
        transformer.transform(xmlSource, outputTarget);
    }

}
