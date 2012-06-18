package info.mikaelsvensson.docutil.xml.documentcreator;

import info.mikaelsvensson.docutil.AbstractDocumentCreatorTest;
import info.mikaelsvensson.docutil.ClassA;
import org.junit.Test;

import java.util.HashMap;

public class StandardDocumentCreatorTest extends AbstractDocumentCreatorTest {
    /**
     * Sample comment with a nice picture of a cloud: {@image resources/cloud.png}.
     * <p/>
     * Class:
     * {@embed class info.mikaelsvensson.docutil.ClassA}
     *
     * Result:
     * {@embed file resources/ClassA.StandardDocumentCreator.xml}
     */
    @Test
    public void testClassA() throws Exception {
        performTest(ClassA.class, new StandardDocumentCreator(new HashMap<String, String>()));
    }
}
