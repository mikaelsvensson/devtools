package info.mikaelsvensson.devtools.shared;

import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorFactory;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorFactoryException;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.EnumDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.StandardDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.extensive.ExtensiveDocumentCreator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DocumentCreatorFactoryTest {
    @Test
    public void createStandard() throws Exception {
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(StandardDocumentCreator.NAME)
                        instanceof StandardDocumentCreator);
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(StandardDocumentCreator.class.getName())
                        instanceof StandardDocumentCreator);
    }

    @Test
    public void createExtensive() throws Exception {
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(ExtensiveDocumentCreator.NAME)
                        instanceof ExtensiveDocumentCreator);
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(ExtensiveDocumentCreator.class.getName())
                        instanceof ExtensiveDocumentCreator);
    }

    @Test
    public void createEnum() throws Exception {
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(EnumDocumentCreator.NAME)
                        instanceof EnumDocumentCreator);
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(EnumDocumentCreator.class.getName())
                        instanceof EnumDocumentCreator);
    }

    @Test
    public void createElementsOnly() throws Exception {
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(ElementsOnlyDocumentCreator.NAME)
                        instanceof ElementsOnlyDocumentCreator);
        assertTrue(
                DocumentCreatorFactory.getDocumentCreator(ElementsOnlyDocumentCreator.class.getName())
                        instanceof ElementsOnlyDocumentCreator);
    }

    @Test(expected = DocumentCreatorFactoryException.class)
    public void failToCreateMissing() throws Exception {
        DocumentCreatorFactory.getDocumentCreator("invalid name");
    }
}
