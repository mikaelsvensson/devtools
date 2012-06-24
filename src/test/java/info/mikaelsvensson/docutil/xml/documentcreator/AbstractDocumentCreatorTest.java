package info.mikaelsvensson.docutil.xml.documentcreator;

import info.mikaelsvensson.docutil.shared.DocumentCreator;
import info.mikaelsvensson.docutil.shared.DocumentCreatorFactory;
import info.mikaelsvensson.docutil.xml.XmlDoclet;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertTrue;

public abstract class AbstractDocumentCreatorTest {

    @Before
    public void setUp() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }

    protected void performTest(String documentCreatorId, Class testClass, String... documentCreatorArgs) throws IOException, URISyntaxException, SAXException, ParserConfigurationException {

        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(documentCreatorId);

        String testClassFileName = new File(".\\src\\test\\resources\\" + testClass.getName().replace('.', File.separatorChar) + ".java").getAbsolutePath();
        File actualFile = File.createTempFile("xmldoclet-test-" + testClass.getName() + "-" + documentCreator.getClass().getSimpleName() + "-", ".xml");

        String[] args = new String[7 + documentCreatorArgs.length + 1];
        int i = 0;
        args[i++] = "-doclet";
        args[i++] = XmlDoclet.class.getName();
        args[i++] = "-private";
        args[i++] = "-action.1.format.name";
        args[i++] = documentCreatorId;
        args[i++] = "-action.1.output";
        args[i++] = actualFile.getAbsolutePath();
        for (int x = 0; x < documentCreatorArgs.length; x++) {
            args[i++] = documentCreatorArgs[x];
        }
        args[i] = testClassFileName;

        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                args);

//        System.out.println(FileUtil.getFileContent(actualFile));

        File expectedFile = new File("target\\test-classes\\" + testClass.getName().replace('.', File.separatorChar) + "." + documentCreatorId + ".xml");
        Diff diff = new Diff(new FileReader(expectedFile), new FileReader(actualFile));

        actualFile.delete();

        assertTrue(diff.identical());
    }
}
