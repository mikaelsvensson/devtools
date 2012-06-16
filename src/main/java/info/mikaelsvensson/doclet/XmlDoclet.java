package info.mikaelsvensson.doclet;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.doclet.documentcreator.StandardDocumentCreator;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.Map;

public class XmlDoclet {
    private static XmlDocletOptions options;

    public static boolean start(RootDoc root) {
        options = XmlDocletOptions.load(root.options());

        try {
            DocumentCreator documentCreator = options.getDocumentCreator();

            Document document = documentCreator.generateDocument(root);
            for (XmlDocletAction action : options.getActions().values()) {
                write(document, action.getOutput(), action.getTransformer(), action.getParameters());
            }
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    public static void write(Document doc, File file, File transformerFile, final Map<String, String> parameters) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        if (null != transformerFile) {
            transformer = transformerFactory.newTransformer(new StreamSource(transformerFile));
        } else {
            transformer = transformerFactory.newTransformer();
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue());
        }

        transformer.setParameter("outputFile", file.getName());
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
