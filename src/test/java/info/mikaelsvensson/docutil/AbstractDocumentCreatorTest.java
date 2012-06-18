package info.mikaelsvensson.docutil;

import info.mikaelsvensson.docutil.shared.DocumentCreator;
import info.mikaelsvensson.docutil.xml.XmlDoclet;
import info.mikaelsvensson.docutil.xml.XmlDocletAction;
import info.mikaelsvensson.docutil.xml.XmlDocletOptions;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public abstract class AbstractDocumentCreatorTest {

    @Before
    public void setUp() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }

    protected void performTest(Class testClass, DocumentCreator documentCreator) throws IOException, URISyntaxException, SAXException, ParserConfigurationException {
        String testClassFileName = new File(".\\src\\test\\java\\" + testClass.getName().replace('.', File.separatorChar) + ".java").getAbsolutePath();
        File actualFile = File.createTempFile("xmldoclet-test-" + testClass.getSimpleName() + "-" + documentCreator.getClass().getSimpleName() + "-", ".xml");
        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                XmlDoclet.class.getName(),
                new String[]{
                        "-doclet",
                        XmlDoclet.class.getName(),
                        "-private",
                        XmlDocletOptions.PARAMETER_FORMAT,
                        XmlDocletAction.Format.STANDARD.simpleName(),
                        XmlDocletOptions.PARAMETER_OUTPUT,
                        actualFile.getAbsolutePath(),
                        XmlDocletOptions.PARAMETER_FORMAT_PROPERTY,
                        "showFields=false",
                        testClassFileName
                });

        File expectedFile = new File("target\\test-classes\\" + testClass.getSimpleName() + "." + documentCreator.getClass().getSimpleName() + ".xml");
        Diff diff = new Diff(new FileReader(expectedFile), new FileReader(actualFile));
//        actualFile.delete();

        assertTrue(diff.identical());
    }
}
