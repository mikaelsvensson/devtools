package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ConstructorDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ConstructorDocHandler extends ExecutableMemberDocHandler<ConstructorDoc> {
// --------------------------- CONSTRUCTORS ---------------------------

    ConstructorDocHandler(final Dispatcher dispatcher) {
        super(ConstructorDoc.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final ConstructorDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);
    }
}
