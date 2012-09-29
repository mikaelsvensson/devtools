package info.mikaelsvensson.doctools.xml.documentcreator;

import info.mikaelsvensson.doctools.doclet.shared.DocumentCreator;
import info.mikaelsvensson.doctools.doclet.shared.DocumentCreatorFactory;
import info.mikaelsvensson.doctools.doclet.xml.XmlDoclet;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.fail;

public abstract class AbstractDocumentCreatorTest {

    @Before
    public void setUp() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }

    protected void performTest(String documentCreatorId, final Class testClass, String... documentCreatorArgs) throws IOException, URISyntaxException, SAXException, ParserConfigurationException {

        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(documentCreatorId);

        String testClassFileName = new File("src" + File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar + testClass.getName().replace('.', File.separatorChar) + ".java").getAbsolutePath();
        File actualFile = File.createTempFile("xmldoclet-test-" + testClass.getName() + "-" + documentCreator.getClass().getSimpleName() + "-", ".xml");

        String[] args = new String[7 + documentCreatorArgs.length + 1];
        int i = 0;
        args[i++] = "-doclet";
        args[i++] = XmlDoclet.class.getName();
        args[i++] = "-private";
        args[i++] = "-format.name";
        args[i++] = documentCreatorId;
        args[i++] = "-output";
        args[i++] = actualFile.getAbsolutePath();
        for (int x = 0; x < documentCreatorArgs.length; x++) {
            args[i++] = documentCreatorArgs[x];
        }
        args[i] = testClassFileName;

        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                args);

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        File expectedFile = new File("target" + File.separatorChar + "test-classes" + File.separator + testClass.getName().replace('.', File.separatorChar) + "." + documentCreatorId + ".xml");

        Document expectedDoc = loadSingleClassElement(testClass, documentBuilder, expectedFile);

        Document actualDoc = loadSingleClassElement(testClass, documentBuilder, actualFile);

        Diff diff = new Diff(expectedDoc, actualDoc);

        actualFile.delete();

        boolean identical = diff.identical();
        if (!identical) {
            StringBuffer sb = new StringBuffer();
            diff.appendMessage(sb);
            fail(sb.toString());
        }
        //assertTrue(identical);
    }

    private Document loadSingleClassElement(Class testClass, DocumentBuilder documentBuilder, File sourceFile) throws SAXException, IOException {
        Document expectedDoc = documentBuilder.parse(sourceFile);
        Node expectedNode = findClassElement(testClass, expectedDoc);
        Document doc2 = documentBuilder.newDocument();
        doc2.appendChild(doc2.importNode(expectedNode, true));
        return doc2;
    }

    protected abstract Node findClassElement(final Class cls, final Document doc);

    protected static Node findClassElementByQName(final Class cls, final Document doc, final String classElementName, final String qnameAttributeName) {
        NodeList classElements = doc.getDocumentElement().getElementsByTagName(classElementName);
        for (int j = 0; j < classElements.getLength(); j++) {
            Element classEl = (Element) classElements.item(j);
            if (classEl.getAttribute(qnameAttributeName).equals(cls.getName())) {
                return classEl;
            }
        }
        return null;
    }
}
