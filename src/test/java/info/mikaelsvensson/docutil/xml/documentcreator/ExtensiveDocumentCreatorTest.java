package info.mikaelsvensson.docutil.xml.documentcreator;

import enumeration.Fruit;
import info.mikaelsvensson.docutil.ClassA;
import info.mikaelsvensson.docutil.Contact;
import info.mikaelsvensson.docutil.Vehicle;
import info.mikaelsvensson.docutil.xml.extensivedocumentcreator.ExtensiveDocumentCreator;
import org.junit.Ignore;
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

    @Test
    @Ignore
    public void testContact() throws Exception {
        performTest(
                Contact.class,
                "-action.1.format.property." + ExtensiveDocumentCreator.SIMPLE_TYPE_DATA,
                "true");
    }

    @Test
    public void testVehicle() throws Exception {
        performTest(
                Vehicle.class,
//                "-action.1.format.property." + ExtensiveDocumentCreator.WRAP_LIST_ELEMENTS,
//                "false",
                "-action.1.format.property." + ExtensiveDocumentCreator.CLASS_MEMBER_TYPE_FILTER,
                "si",
                "-action.1.format.property." + ExtensiveDocumentCreator.INTERFACE_MEMBER_TYPE_FILTER,
                "si");
    }

    private void performTest(final Class<?> testClass, String... documentCreatorArgs) throws IOException, URISyntaxException, SAXException, ParserConfigurationException {
        performTest(ExtensiveDocumentCreator.NAME, testClass, documentCreatorArgs);
    }
}
