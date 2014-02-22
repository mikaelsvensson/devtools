package info.mikaelsvensson.devtools.xml.documentcreator;

import info.mikaelsvensson.devtools.ClassA;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.StandardDocumentCreator;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class StandardDocumentCreatorTest extends AbstractDocumentCreatorTest {
    /**
     * Sample comment with a nice picture of a cloud: {@image resources/cloud.png}.
     * <p/>
     * Class:
     * {@embed class info.mikaelsvensson.devtools.ClassA}
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
