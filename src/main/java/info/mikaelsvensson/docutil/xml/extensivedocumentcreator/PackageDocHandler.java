package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.PackageDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class PackageDocHandler extends Handler<PackageDoc> {

    PackageDocHandler() {
        super(PackageDoc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final PackageDoc doc) {
        super.handleImpl(el, doc);
    }

}
