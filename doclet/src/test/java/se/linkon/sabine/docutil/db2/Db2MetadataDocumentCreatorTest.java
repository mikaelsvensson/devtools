package se.linkon.sabine.docutil.db2;

import org.junit.Before;
import org.junit.Test;
import se.linkon.sabine.docutil.shared.DocumentCreator;
import se.linkon.sabine.docutil.shared.DocumentCreatorFactory;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Db2MetadataDocumentCreatorTest {
    @Before
    public void setUp() throws Exception {
        Db2MetadataDocumentCreator.register();
    }

    @Test
    public void createElementsOnly() throws Exception {
        DocumentCreator documentCreator = DocumentCreatorFactory.getDocumentCreator(Db2MetadataDocumentCreator.NAME);
        assertThat(documentCreator, is(Db2MetadataDocumentCreator.class));
    }
}
