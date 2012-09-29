package info.mikaelsvensson.doctools.xml.documentcreator;

import info.mikaelsvensson.doctools.ClassA;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class StandardDocumentCreatorTest extends AbstractDocumentCreatorTest {
    /**
     * Sample comment with a nice picture of a cloud: {@image resources/cloud.png}.
     * <p/>
     * Class:
     * {@embed class info.mikaelsvensson.doctools.ClassA}
     *
     * Result:
     * {@embed file resources/ClassA.standard.xml}
     */
    @Test
    public void testClassA() throws Exception {
        performTest(StandardDocumentCreator.NAME, ClassA.class);
    }
    @Override
    protected Node findClassElement(final Class cls, final Document doc) {
        return AbstractDocumentCreatorTest.findClassElementByQName(cls, doc, "class", "qualified-name");
    }
}
