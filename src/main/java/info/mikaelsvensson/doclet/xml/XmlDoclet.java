package info.mikaelsvensson.doclet.xml;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.doclet.AbstractDoclet;
import info.mikaelsvensson.doclet.xml.documentcreator.StandardDocumentCreator;
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
import java.io.InputStream;

public class XmlDoclet extends AbstractDoclet {
    private static XmlDocletOptions options;

    protected XmlDoclet(RootDoc root) {
        super(root);
    }

    public static boolean start(RootDoc root) {
        options = XmlDocletOptions.load(root.options());

        System.out.println("#############################################################################################");

        try {

            for (XmlDocletAction action : options.getActions().values()) {
                
                root.printNotice("Building XML document.");
                Document document = action.getFormat().createDocumentCreator().generateDocument(root);
                root.printNotice("Finished building XML document.");
                
                generate(document, action.getOutput(), action.getTransformer(), root);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    public static void generate(Document doc, File outputFile, File xsltFile, RootDoc root) throws ParserConfigurationException {
        try {
            if (xsltFile != null) {
                root.printNotice("Transforming XML document using XSLT");
                writeFile(doc, outputFile, xsltFile);
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
    public static void writeFile(Document doc, File file, File xsltFile) throws TransformerException {
        Source xsltSource = new StreamSource(xsltFile);
        writeFile(doc, file, xsltSource);
    }

    public static void writeFile(Document doc, File file, InputStream xsltStream) throws TransformerException {
        Source xsltSource = new StreamSource(xsltStream);
        writeFile(doc, file, xsltSource);
    }

    private static void writeFile(Document doc, File file, Source xsltSource) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);
        StreamResult outputTarget = new StreamResult(file);
        Source xmlSource = new DOMSource(doc);
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
                        StandardDocumentCreator.class.getSimpleName(),
//                        ElementsOnlyDocumentCreator.class.getSimpleName(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        "out.info.mikaelsvensson.doclet.xml",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java\\info\\mikaelsvensson\\doclet\\XmlDocletTest.java"
                });
    }
}
