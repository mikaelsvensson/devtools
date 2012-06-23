package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.AnnotationTypeDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class AnnotationTypeDocHandler extends DocHandler<AnnotationTypeDoc> {

    AnnotationTypeDocHandler() {
        super(AnnotationTypeDoc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final AnnotationTypeDoc doc) {
        handleDocImpl(el, doc.elements(), "elements", "element");
    }
}
