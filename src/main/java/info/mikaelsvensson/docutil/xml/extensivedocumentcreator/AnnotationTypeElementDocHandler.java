package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.AnnotationTypeElementDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class AnnotationTypeElementDocHandler extends MethodDocHandler<AnnotationTypeElementDoc> {
// --------------------------- CONSTRUCTORS ---------------------------

    AnnotationTypeElementDocHandler(final Dispatcher dispatcher) {
        super(AnnotationTypeElementDoc.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final AnnotationTypeElementDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);
    }
}
