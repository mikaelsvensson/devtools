package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.docutil.shared.DocumentCreatorException;
import info.mikaelsvensson.docutil.shared.DocumentWrapper;
import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;
import info.mikaelsvensson.docutil.xml.documentcreator.AbstractDocumentCreator;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class ExtensiveDocumentCreator extends AbstractDocumentCreator {
// ------------------------------ FIELDS ------------------------------

    public static final String NAME = "extensive";

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface DocumentCreator ---------------------

    @Override
    public Document generateDocument(final RootDoc doc, final PropertySet properties) throws DocumentCreatorException {
        DocumentWrapper dw = null;
        try {
            dw = new DocumentWrapper(createDocument("documentation"));
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException(e);
        }

        Dispatcher dispatcher = new Dispatcher(properties);
        try {
            dispatcher.dispatch(dw, "java", doc);
        } catch (JavadocItemHandlerException e) {
            throw new DocumentCreatorException("Could not parse/process Javadoc. ", e);
        }

        return dw.getDocument();
    }
}
