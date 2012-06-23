package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.AnnotationTypeElementDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class AnnotationTypeElementDocHandler extends DocHandler<AnnotationTypeElementDoc> {

    AnnotationTypeElementDocHandler() {
        super(AnnotationTypeElementDoc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final AnnotationTypeElementDoc doc) {
    }
}
