package info.mikaelsvensson.doclet;

import org.w3c.dom.Document;

public class DocumentWrapper extends ElementWrapper {
    private Document doc;

    public Document getDocument() {
        return doc;
    }

    public DocumentWrapper(final Document doc) {
        super(null);
        this.doc = doc;
        this.el = this.doc.getDocumentElement();
    }

}
