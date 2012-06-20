package se.linkon.sabine.docutil.xml.documentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.w3c.dom.Document;
import se.linkon.sabine.docutil.shared.DocumentCreatorException;
import se.linkon.sabine.docutil.shared.DocumentWrapper;
import se.linkon.sabine.docutil.shared.propertyset.PropertySet;

import javax.xml.parsers.ParserConfigurationException;

public class ElementsOnlyDocumentCreator extends AbstractDocumentCreator {
    public static final String NAME = "elementsonly";

/*
    public ElementsOnlyDocumentCreator() throws ParserConfigurationException {
        super();
    }
*/

    @Override
    public Document generateDocument(final RootDoc doc, final PropertySet properties) throws DocumentCreatorException {

        DocumentWrapper documentWrapper = null;
        try {
            documentWrapper = new DocumentWrapper(createDocument("classes"));
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException(e);
        }

        for (ClassDoc classDoc : doc.classes()) {
            documentWrapper.addChild("class").addChild("name").setText(classDoc.qualifiedName());
        }
        return documentWrapper.getDocument();
    }
}
