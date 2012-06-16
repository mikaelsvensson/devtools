package info.mikaelsvensson.doclet.xml;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.doclet.AbstractDoclet;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.Map;

public class XmlDoclet<O extends XmlDocletOptions> extends AbstractDoclet {

    private O options;

    protected XmlDoclet(RootDoc root, O options) {
        super(root);
        this.options = options;
    }

    public static boolean start(RootDoc root) {
        return new XmlDoclet(root, new XmlDocletOptions(root.options())).generate();
    }

    public boolean generate() {
        try {

            for (XmlDocletAction action : options.getActions().values()) {

                root.printNotice("Building XML document.");
                Document document = action.getDocumentCreator().generateDocument(root);
                root.printNotice("Finished building XML document.");

                generate(document, action.getOutput(), action.getTransformer(), action.getParameters());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    public void generate(Document doc, File outputFile, File xsltFile, Map<String, String> parameters) throws ParserConfigurationException {
        try {
            if (xsltFile != null) {
                root.printNotice("Transforming XML document using XSLT");
                writeFile(doc, outputFile, xsltFile, parameters);
                root.printNotice("Transformed XML document saved as " + outputFile.getAbsolutePath());
            }
            else {
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
        return XmlDocletOptions.optionLength(option);
    }

    public static void main(String[] args) {
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.Format.STANDARD.name(),
//                        ElementsOnlyDocumentCreator.class.getSimpleName(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        "out.info.mikaelsvensson.doclet.xml",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java\\info\\mikaelsvensson\\doclet\\XmlDocletTest.java"
                });
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.Format.STANDARD.name(),
//                        ElementsOnlyDocumentCreator.class.getSimpleName(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        "index2.html",
                        XmlDocletOptions.PARAMETER_TRANSFORMER,
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\resources\\multiple-files.xslt",
                        "-sourcepath",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\main\\java",
                        "-subpackages",
                        "info.mikaelsvensson.doclet"
//                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java\\info\\mikaelsvensson\\doclet\\XmlDocletTest.java"
                });
    }
}
