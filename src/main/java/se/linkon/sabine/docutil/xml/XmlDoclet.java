package se.linkon.sabine.docutil.xml;

import com.sun.javadoc.RootDoc;
import org.w3c.dom.Document;
import se.linkon.sabine.docutil.AbstractDoclet;
import se.linkon.sabine.docutil.shared.DocumentCreatorException;
import se.linkon.sabine.docutil.shared.FileUtil;
import se.linkon.sabine.docutil.shared.propertyset.PropertySet;
import se.linkon.sabine.docutil.shared.propertyset.PropertySetException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class XmlDoclet<A extends XmlDocletAction, O extends XmlDocletOptions<A>> extends AbstractDoclet {

    private O options;

    protected XmlDoclet(RootDoc root, O options) {
        super(root);
        this.options = options;
    }

    public static boolean start(RootDoc root) throws PropertySetException {
        return new XmlDoclet(root, new XmlDocletOptions(new PropertySet(root.options()))).generate();
    }

    public boolean generate() {
        for (Map.Entry<String, A> entry : options.getActions().entrySet()) {
            try {
                A action = entry.getValue();

                root.printNotice("Building XML document.");
                Document document = action.createDocumentCreator(action.getParameters()).generateDocument(root);
                root.printNotice("Finished building XML document.");

                generate(document, action.getOutput(), action.getTransformer(), action.getParameters());

                postProcess(action);
            } catch (IOException e) {
                printError(new DocumentCreatorException("Could not post-process action " + entry.getKey() + ".", e));
            } catch (DocumentCreatorException e) {
                printError(e);
            }
        }
        return true;
    }

    //TODO: MISV 20120618 Refactor postProcess into the same kind of "factory mechanism" used for DocumentCreator. Perhaps call it PostProcessor?
    private void postProcess(XmlDocletAction action) throws IOException {
        if (action.getPostProcessor() != null && action.getPostProcessor().length() > 0) {
            String folderExpr = action.getPostProcessingParameters().get("folder");
            String templateFileExpr = action.getPostProcessingParameters().get("templateFile");
            String filePatternExpr = action.getPostProcessingParameters().get("filePattern");
            String replaceStringExpr = action.getPostProcessingParameters().get("replaceString");

            System.out.println(action.getPostProcessingParameters());

            File folder = new File(folderExpr);
            Pattern filePattern = Pattern.compile(filePatternExpr);
            File templateFile = new File(templateFileExpr);

            String template = FileUtil.getFileContent(templateFile);

            List<File> files = FileUtil.findFiles(folder, filePattern);
            for (File file : files) {
                System.out.println("Look for " + replaceStringExpr + " in " + templateFile.getName() + " and apply it to " + file.getName());

                String source = FileUtil.getFileContent(file);
                String result = template.replace(replaceStringExpr, source);
                FileWriter writer = new FileWriter(file);
                writer.write(result);
                writer.close();

                System.out.println(file.getAbsolutePath());
            }
        }
    }

    public void generate(Document doc, File outputFile, File xsltFile, Map<String, String> parameters) {
        try {
            if (xsltFile != null) {
                root.printNotice("Transforming XML document using XSLT");
                writeFile(doc, outputFile, xsltFile, parameters);
                root.printNotice("Transformed XML document saved as " + outputFile.getAbsolutePath());
            } else {
                root.printNotice("Saving XML document");
                writeFile(doc, outputFile);
                root.printNotice("XML document saved as " + outputFile.getAbsolutePath());
            }
        } catch (TransformerException e) {
            root.printError(e.getMessage());
        }
    }

    public static void writeFile(Document doc, File file, File xsltFile, Map<String, String> parameters) throws TransformerException {
        Source xsltSource = new StreamSource(xsltFile);
        writeFile(doc, file, xsltSource, parameters);
    }

    private static void writeFile(Document doc, File file, Source xsltSource, Map<String, String> parameters) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);
        StreamResult outputTarget = new StreamResult(file);

        Source xmlSource = new DOMSource(doc);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue());
        }

        transformer.setParameter("outputFile", file.getName());
        transformer.transform(xmlSource, outputTarget);
    }

    public static void writeFile(Document doc, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(file));
        System.out.println(file.getAbsolutePath());
    }

    public static int optionLength(String option) {
        int len = XmlDocletOptions.optionLength(option);
        return len;
//        return len > 0 ? len : Standard.optionLength(option);
    }

    public static void main(String[] args) {
/*
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.Format.STANDARD.name(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        "out.xml",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java\\info\\mikaelsvensson\\doclet\\XmlDocletTest.java"
                });
*/
/*
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.FORMAT_STANDARD,
//                        ElementsOnlyDocumentCreator.class.getSimpleName(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        "index2.html",
                        XmlDocletOptions.PARAMETER_TRANSFORMER,
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\resources\\multiple-files.xslt",
                        "-sourcepath",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java",
//                        "D:\\Dokument\\Utveckling\\doclet\\src\\main\\java",
                        "-subpackages",
                        "se.linkon.sabine.doclet"
                });
*/
    }
}
