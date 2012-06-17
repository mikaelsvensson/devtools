package info.mikaelsvensson.doclet.xml.documentcreator;

import info.mikaelsvensson.doclet.AbstractDocumentCreatorTest;
import info.mikaelsvensson.doclet.ClassA;
import org.junit.Test;

import java.util.HashMap;

public class StandardDocumentCreatorTest extends AbstractDocumentCreatorTest {
    @Test
    public void testClassA() throws Exception {
        performTest(ClassA.class, new StandardDocumentCreator(new HashMap<String, String>()));
    }
}
