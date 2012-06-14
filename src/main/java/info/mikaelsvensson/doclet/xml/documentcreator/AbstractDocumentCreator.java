package info.mikaelsvensson.doclet.xml.documentcreator;

import info.mikaelsvensson.doclet.shared.DocumentCreator;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public abstract class AbstractDocumentCreator implements DocumentCreator {
    private DocumentBuilder documentBuilder;

    protected AbstractDocumentCreator() throws ParserConfigurationException {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    protected Document createDocument(final String rootElementName) {
        Document doc = documentBuilder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));
        return doc;
    }

}
