package info.mikaelsvensson.doclet;

import info.mikaelsvensson.doclet.shared.DocumentCreator;
import info.mikaelsvensson.doclet.xml.XmlDoclet;
import info.mikaelsvensson.doclet.xml.XmlDocletAction;
import info.mikaelsvensson.doclet.xml.XmlDocletOptions;
import info.mikaelsvensson.doclet.xml.documentcreator.StandardDocumentCreator;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class XmlDocletTest {
    @Test
    @Ignore
    public void testClass1() throws Exception {
        performTest(ClassA.class, new StandardDocumentCreator());
    }

    private void performTest(Class testClass, DocumentCreator documentCreator) throws IOException, URISyntaxException, SAXException, ParserConfigurationException {
        String testClassFileName = new File(".").getAbsolutePath() + "\\src\\test\\java\\" + testClass.getName().replace('.', File.separatorChar) + ".java";
        File actualFile = File.createTempFile("xmldoclet-test-" + testClass.getSimpleName() + "-" + documentCreator.getClass().getSimpleName() + "-", ".xml");
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.Format.STANDARD.simpleName(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        actualFile.getAbsolutePath(),
                        testClassFileName
                });
        String actualContent = readFile(actualFile);
//        Document actualDoc = loadDocument(actualFile);
        File expectedFile = new File("target\\test-classes\\" + testClass.getSimpleName() + "." + documentCreator.getClass().getSimpleName() + ".xml");
        String expectedContent = readFile(expectedFile);
//        Document expectedDoc = loadDocument(expectedFile);
        assertEquals(expectedContent, actualContent);
//        assertDocumentEquals(expectedDoc, actualDoc);
        actualFile.delete();
    }

/*
    private void assertDocumentEquals(final Document expectedDoc, final Document actualDoc) {
        assertElementEquals(expectedDoc.getDocumentElement(), actualDoc.getDocumentElement());
    }
*/

/*
    private void assertElementEquals(final Element expected, final Element actual) {
        assertEquals(expected.getTagName(), actual.getTagName());
        for (int i=0; i < expected.getAttributes().getLength(); i++) {
            Node attrNode = expected.getAttributes().item(1);
            String attrName = attrNode.getNodeName();
            assertEquals(attrNode.getNodeValue(), actual.getAttribute(attrName));
        }
//        int iChildExp = 0;
        int iChildAct = 0;
        for (int iChildExp = 0; iChildExp < expected.getChildNodes().getLength(); iChildExp++) {
            Node expectedNode = expected.getChildNodes().item(i);
            while (actual.getChildNodes().item(iChildAct++))
            if (expectedNode.getNodeType() == Node.ELEMENT_NODE) {
                Node actualNode = actual.getChildNodes().item(i);
                assertElementEquals((Element)expectedNode, (Element) actualNode);
            }
        }
    }
*/

    private String readFile(final File actualFile) throws IOException {
        FileReader reader = new FileReader(actualFile);
        char[] content = new char[(int) actualFile.length()];
        reader.read(content, 0, content.length);
        String s = new String(content).replaceAll(">[\\n\\s]*<", "><").replaceAll("\\s/>", "/>").replaceAll("\\s+", " ");
        return s.substring(s.indexOf("?>"));
    }

    private Document loadDocument(File file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return documentBuilder.parse(file);
    }
}
