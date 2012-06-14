package info.mikaelsvensson.doclet.shared;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class ElementWrapper {
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

    public ElementWrapper setText(final String text) {
        el.appendChild(el.getOwnerDocument().createTextNode(text));
        return this;
    }

    public ElementWrapper addChildWithText(String name, String text, String... attributeValuePairs) {
        Element child = el.getOwnerDocument().createElement(name);

        for (int i = 1; i < attributeValuePairs.length; i += 2) {
            String attrName = attributeValuePairs[i - 1];
            String attrValue = attributeValuePairs[i];

            child.setAttribute(attrName, attrValue);
        }
        if (null != text && text.length() > 0) {
            child.appendChild(el.getOwnerDocument().createTextNode(text));
        }
        el.appendChild(child);
        return new ElementWrapper(child);
    }

    public String getTagName() {
        return el.getTagName();
    }

    public String getAttribute(String name) {
        return el.getAttribute(name);
    }
}
