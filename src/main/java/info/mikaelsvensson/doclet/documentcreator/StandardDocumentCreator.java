package info.mikaelsvensson.doclet.documentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.doclet.DocumentWrapper;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class StandardDocumentCreator extends AbstractDocumentCreator {
    public StandardDocumentCreator() throws ParserConfigurationException {
        super();
    }

    @Override
    public Document generateDocument(final RootDoc doc) {

        DocumentWrapper documentWrapper = new DocumentWrapper(createDocument("classes"));
        for (ClassDoc classDoc : doc.classes()) {
            documentWrapper.addChild("class").setAttr("name", classDoc.name());
        }
        return documentWrapper.getDocument();
    }
}
