package se.linkon.sabine.docutil.xml.documentcreator;

import org.junit.Ignore;
import org.junit.Test;
import se.linkon.sabine.docutil.ClassA;

public class StandardDocumentCreatorTest extends AbstractDocumentCreatorTest {
    /**
     * Sample comment with a nice picture of a cloud: {@image resources/cloud.png}.
     * <p/>
     * Class:
     * {@embed class info.mikaelsvensson.docutil.ClassA}
     *
     * Result:
     * {@embed file resources/se.linkon.sabine.docutil.ClassA.StandardDocumentCreator.xml}
     */
    @Test
    @Ignore
    public void testClassA() throws Exception {
        performTest(ClassA.class, StandardDocumentCreator.NAME);
    }
}
