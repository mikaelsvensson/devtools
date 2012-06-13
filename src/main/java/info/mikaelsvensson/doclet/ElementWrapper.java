package info.mikaelsvensson.doclet;

import org.w3c.dom.Element;

public class ElementWrapper {
    protected Element el;

    public ElementWrapper(final Element el) {
        this.el = el;
    }

    public ElementWrapper setAttr(String name, String value) {
        el.setAttribute(name, value);
        return this;
    }

    public ElementWrapper createElement(String name) {
        return new ElementWrapper(el.getOwnerDocument().createElement(name));
    }

    public ElementWrapper addChild(String name) {
        Element child = el.getOwnerDocument().createElement(name);
        el.appendChild(child);
        return new ElementWrapper(child);
    }

    public ElementWrapper setText(final String text) {
        el.appendChild(el.getOwnerDocument().createTextNode(text));
        return this;
    }
}
