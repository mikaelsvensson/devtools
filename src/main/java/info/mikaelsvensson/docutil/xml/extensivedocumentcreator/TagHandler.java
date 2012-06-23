package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class TagHandler<T extends Tag> extends Handler<T> {

    TagHandler() {
        super((Class<T>) Tag.class);
    }

    public TagHandler(final Class<T> docClass) {
        super(docClass);
    }

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                "name", doc.name(),
                "kind", doc.kind(),
                "text", doc.text());
    }
}
