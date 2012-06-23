package info.mikaelsvensson.docutil.xml.documentcreator;

import enumeration.Fruit;
import info.mikaelsvensson.docutil.ClassA;
import info.mikaelsvensson.docutil.xml.extensivedocumentcreator.ExtensiveDocumentCreator;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

public class ExtensiveDocumentCreatorTest extends AbstractDocumentCreatorTest {
    @Test
    public void testClassA() throws Exception {
        performTest(ClassA.class);
    }
    @Test
    public void testFruit() throws Exception {
        performTest(Fruit.class);
    }

    private void performTest(final Class<?> testClass, String... documentCreatorArgs) throws IOException, URISyntaxException, SAXException, ParserConfigurationException {
        performTest(testClass, ExtensiveDocumentCreator.NAME, documentCreatorArgs);
    }
}
