package info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2;

import info.mikaelsvensson.devtools.doclet.shared.DocumentCreator;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorFactory;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Db2MetadataDocumentCreatorTest
{
    @Test
    public void createElementsOnly() throws Exception
    {
        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(Db2MetadataDocumentCreator.NAME);
        assertThat(documentCreator, is(Db2MetadataDocumentCreator.class));
    }
}
