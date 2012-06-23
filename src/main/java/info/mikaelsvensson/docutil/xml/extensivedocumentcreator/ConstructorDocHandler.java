package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ConstructorDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ConstructorDocHandler extends DocHandler<ConstructorDoc> {

    ConstructorDocHandler() {
        super(ConstructorDoc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final ConstructorDoc doc) {
    }
}
