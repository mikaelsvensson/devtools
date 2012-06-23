package info.mikaelsvensson.docutil.shared;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.commenttext.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

public class ElementWrapper {
    private InlineTagHandler[] inlineTagHandlers = new InlineTagHandler[]{
            new CodeInlineTagHandler(),
            new ImageInlineTagHandler(),
            new LinkInlineTagHandler(),
            new SourceFileInlineTagHandler(),
            new GenericInlineTagHandler()};
    public static final Pattern COMMENT_PARAGRAPH = Pattern.compile("<(p|br)\\s*/\\s*>");

    protected Element el;
    private static DocumentBuilder docBuilder;

    static {
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public ElementWrapper(final Element el) {
        this.el = el;
    }

    public ElementWrapper setAttribute(String name, String value) {
        el.setAttribute(name, value);
        return this;
    }

    public ElementWrapper createElement(String name) {
        return new ElementWrapper(el.getOwnerDocument().createElement(name));
    }

    public ElementWrapper addChild(String name, String... attributeValuePairs) {
        return addChildWithText(name, null, attributeValuePairs);
    }

    public ElementWrapper addChildFromSource(String source) throws IOException, SAXException {
        String xmlSource = "<?xml version=\"1.0\"?>" + source;
        Document tempDoc = docBuilder.parse(new InputSource(new StringReader(xmlSource)));
        Element element = (Element) el.getOwnerDocument().importNode(tempDoc.getDocumentElement(), true);
        el.appendChild(element);
        return new ElementWrapper(element);
    }

    public ElementWrapper addCommentChild(Doc doc) throws InlineTagHandlerException {
        return addCommentChild(doc.inlineTags());
    }

    public ElementWrapper addCommentChild(Tag doc) throws InlineTagHandlerException {
        return addCommentChild(doc.inlineTags());
    }

    private ElementWrapper addCommentChild(Tag[] inlineTags) throws InlineTagHandlerException {
        StringBuilder sb = new StringBuilder();

        tag:
        for (Tag tag : inlineTags) {
            for (InlineTagHandler handler : inlineTagHandlers) {
                if (handler.handles(tag)) {
                    sb.append(handler.toString(tag));
                    continue tag;
                }
            }
            sb.append(tag.text());
        }
        return addCommentChild(sb.toString());
    }

    private ElementWrapper addCommentChild(String comment) {
        String c = comment != null && comment.length() > 0 ? comment.replace("\n", "") : "";
        if (c.length() > 0) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("<comment format=\"html\">");
                // TODO: MISV 20120621 It would be nice if this method could also fix the cause of the '"p" must be terminated by the matching end-tag' issue. The problem stems from invalid HTML code genereated by wsimport/wsgen (?). Solve by counting the number of '</p' between one '<p' and the next (count=0 => prepend '</p>' before the next '<p')?
                for (String paragraph : COMMENT_PARAGRAPH.split(c)) {
                    sb.append("<p>").append(paragraph).append("</p>");
                }
                sb.append("</comment>");
                addChildFromSource(sb.toString());
            } catch (IOException e) {
                addChildWithText("comment", comment, "format", "text");
            } catch (SAXException e) {
                addChildWithText("comment", comment, "format", "text");
            }
        }
        return this;
    }

    public ElementWrapper setText(final String text) {
        el.appendChild(el.getOwnerDocument().createTextNode(text));
        return this;
    }

    public ElementWrapper addChildWithText(String name, String text, String... attributeValuePairs) {
        Element child = el.getOwnerDocument().createElement(name);


        if (null != text && text.length() > 0) {
            child.appendChild(el.getOwnerDocument().createTextNode(text));
        }
        el.appendChild(child);
        ElementWrapper wrapper = new ElementWrapper(child);

        wrapper.setAttributes(attributeValuePairs);

        return wrapper;
    }

    public void setAttributes(final String... attributeValuePairs) {
        for (int i = 1; i < attributeValuePairs.length; i += 2) {
            String attrName = attributeValuePairs[i - 1];
            String attrValue = attributeValuePairs[i];

            setAttribute(attrName, attrValue);
        }
    }

    public String getTagName() {
        return el.getTagName();
    }

    public String getAttribute(String name) {
        return el.getAttribute(name);
    }

    public void remoteAttributes(final String... attributesNames) {
        for (String name : attributesNames) {
            el.removeAttribute(name);
        }
    }
}
