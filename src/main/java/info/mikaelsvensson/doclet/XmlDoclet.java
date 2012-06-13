package info.mikaelsvensson.doclet;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.doclet.documentcreator.StandardDocumentCreator;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlDoclet {
    private static XmlDocletOptions options;

    public static boolean start(RootDoc root) {
        options = XmlDocletOptions.load(root.options());

        try {
            DocumentCreator documentCreator = options.getDocumentCreator();

            Document document = documentCreator.generateDocument(root);
            for (XmlDocletAction action : options.getActions().values()) {
                write(document, action.getOutput());
            }
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    public static void write(Document doc, File file) throws TransformerException {
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
                        "out.xml",
                        "D:\\Dokument\\Utveckling\\doclet\\src\\test\\java\\info\\mikaelsvensson\\doclet\\XmlDocletTest.java"
                });
    }
}
