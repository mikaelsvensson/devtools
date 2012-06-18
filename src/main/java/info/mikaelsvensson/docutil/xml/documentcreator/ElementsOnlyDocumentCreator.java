package info.mikaelsvensson.docutil.xml.documentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.docutil.shared.DocumentWrapper;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class ElementsOnlyDocumentCreator extends AbstractDocumentCreator {
    public ElementsOnlyDocumentCreator() throws ParserConfigurationException {
        super();
    }

    @Override
    public Document generateDocument(final RootDoc doc) {

        DocumentWrapper documentWrapper = new DocumentWrapper(createDocument("classes"));
        for (ClassDoc classDoc : doc.classes()) {
            documentWrapper.addChild("class").addChild("name").setText(classDoc.qualifiedName());
        }
        return documentWrapper.getDocument();
    }
}
