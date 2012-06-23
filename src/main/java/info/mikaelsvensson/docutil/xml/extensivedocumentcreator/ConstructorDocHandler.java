package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ConstructorDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ConstructorDocHandler extends ExecutableMemberDocHandler<ConstructorDoc> {
// --------------------------- CONSTRUCTORS ---------------------------

    ConstructorDocHandler() {
        super(ConstructorDoc.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final ConstructorDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);
    }
}
