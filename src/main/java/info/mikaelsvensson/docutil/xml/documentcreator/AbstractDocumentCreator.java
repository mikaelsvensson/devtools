package info.mikaelsvensson.docutil.xml.documentcreator;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.DocumentCreator;
import info.mikaelsvensson.docutil.shared.DocumentCreatorException;
import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.TagHandlerException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public abstract class AbstractDocumentCreator implements DocumentCreator {
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_Q_NAME = "qualified-name";

    protected Document createDocument(final String rootElementName) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));
        return doc;
    }

    protected void addComment(ElementWrapper parentEl, Doc doc) throws DocumentCreatorException {
        try {
            parentEl.addCommentChild(doc);
        } catch (TagHandlerException e) {
            throw new DocumentCreatorException("Could not parse/process one of the Javadoc tags. ", e);
        }
    }
    protected void addComment(ElementWrapper parentEl, Tag doc) throws DocumentCreatorException {
        try {
            parentEl.addCommentChild(doc);
        } catch (TagHandlerException e) {
            throw new DocumentCreatorException("Could not parse/process one of the Javadoc tags. ", e);
        }
    }
}
