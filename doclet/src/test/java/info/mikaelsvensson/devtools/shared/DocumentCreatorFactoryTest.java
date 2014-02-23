package info.mikaelsvensson.devtools.shared;

import info.mikaelsvensson.devtools.doclet.shared.DocumentCreator;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorFactory;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.EnumDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.StandardDocumentCreator;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class DocumentCreatorFactoryTest
{
    @Test
    public void createStandard() throws Exception
    {
        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(StandardDocumentCreator.NAME);
        assertThat(documentCreator, is(StandardDocumentCreator.class));
    }

    @Test
    public void createEnum() throws Exception
    {
        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(EnumDocumentCreator.NAME);
        assertThat(documentCreator, is(EnumDocumentCreator.class));
    }

    @Test
    public void createElementsOnly() throws Exception
    {
        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(ElementsOnlyDocumentCreator.NAME);
        assertThat(documentCreator, is(ElementsOnlyDocumentCreator.class));
    }

    @Test
    public void failToCreateMissing() throws Exception
    {
        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator("invalid name");
        assertNull(documentCreator);
    }
}
