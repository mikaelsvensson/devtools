package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.MethodDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class MethodDocHandler<T extends MethodDoc> extends ExecutableMemberDocHandler<T> {

    MethodDocHandler() {
        super((Class<T>) MethodDoc.class);
    }

    public MethodDocHandler(final Class<T> docClass) {
        super(docClass);
    }

    @Override
    void handleImpl(final ElementWrapper el, final T doc) {
        super.handleImpl(el, doc);

        el.setAttributes("abstract", Boolean.toString(doc.isAbstract()));

        Handler.process(el, "returns", doc.returnType());

        Handler.process(el, "overrides", doc.overriddenType());
    }
}
