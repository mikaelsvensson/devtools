package info.mikaelsvensson.docutil.xml.documentcreator;

import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.regex.Pattern;

public abstract class AbstractDocumentCreator implements DocumentCreator {
    private TagHandler[] tagHandlers = new TagHandler[]{
            new CodeTagHandler(),
            new ImageTagHandler(),
            new LinkTagHandler(),
            new SourceFileTagHandler()};
    public static final Pattern COMMENT_PARAGRAPH = Pattern.compile("<(p|br)\\s*/\\s*>");
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_Q_NAME = "qualified-name";

    protected Document createDocument(final String rootElementName) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));
        return doc;
    }

    protected void addComment(ElementWrapper parentEl, Tag[] inlineTags, RootDoc root) {
        parentEl.addCommentChild(inlineTags, root);
    }

    private static void addComment(ElementWrapper parentEl, String comment, RootDoc root) {
        parentEl.addCommentChild(comment);
    }
}
