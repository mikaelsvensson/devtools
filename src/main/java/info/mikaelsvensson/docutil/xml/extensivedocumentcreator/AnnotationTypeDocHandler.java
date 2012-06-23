package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.AnnotationTypeDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class AnnotationTypeDocHandler extends ClassDocHandler<AnnotationTypeDoc> {
// --------------------------- CONSTRUCTORS ---------------------------

    AnnotationTypeDocHandler() {
        super(AnnotationTypeDoc.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final AnnotationTypeDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        handleDocImpl(el, doc.elements(), "elements", "element");
    }
}
