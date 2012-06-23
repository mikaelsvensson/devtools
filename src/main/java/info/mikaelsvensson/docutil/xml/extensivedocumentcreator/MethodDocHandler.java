package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.MethodDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class MethodDocHandler extends DocHandler<MethodDoc> {

    MethodDocHandler() {
        super(MethodDoc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final MethodDoc doc) {
        el.setAttributes("abstract", Boolean.toString(doc.isAbstract()));

        DocHandler.process(el, "returns", doc.returnType());

        DocHandler.process(el, "overrides", doc.overriddenType());
    }
}
