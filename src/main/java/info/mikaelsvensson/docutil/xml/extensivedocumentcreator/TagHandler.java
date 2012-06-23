package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class TagHandler<T extends Tag> extends Handler<T> {
// --------------------------- CONSTRUCTORS ---------------------------

    TagHandler(final Dispatcher dispatcher) {
        super((Class<T>) Tag.class, dispatcher);
    }

    public TagHandler(final Class<T> docClass, final Dispatcher dispatcher) {
        super(docClass, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                "name", doc.name(),
                "kind", doc.kind(),
                "text", doc.text());
    }
}
