package info.mikaelsvensson.doclet.xml.documentcreator;

import info.mikaelsvensson.doclet.AbstractDocumentCreatorTest;
import info.mikaelsvensson.doclet.ClassA;
import org.junit.Test;

import java.util.HashMap;

public class StandardDocumentCreatorTest extends AbstractDocumentCreatorTest {
    /**
     * Sample comment with a nice picture of a cloud: {@image resources/cloud.png}.
     * <p/>
     * Class:
     * {@embed class info.mikaelsvensson.doclet.ClassA}
     *
     * Result:
     * {@embed file resources/ClassA.StandardDocumentCreator.xml}
     */
    @Test
    public void testClassA() throws Exception {
        performTest(ClassA.class, new StandardDocumentCreator(new HashMap<String, String>()));
    }
}
