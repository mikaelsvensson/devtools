package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.AnnotationTypeDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class AnnotationTypeDocHandler extends ClassDocHandler<AnnotationTypeDoc> {

    AnnotationTypeDocHandler() {
        super(AnnotationTypeDoc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final AnnotationTypeDoc doc) {
        super.handleImpl(el, doc);

        handleDocImpl(el, doc.elements(), "elements", "element");
    }
}
