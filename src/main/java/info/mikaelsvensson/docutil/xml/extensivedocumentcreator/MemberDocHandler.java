package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.MemberDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class MemberDocHandler extends DocHandler<MemberDoc> {

    MemberDocHandler() {
        super(MemberDoc.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final MemberDoc doc) {
        el.setAttributes("synthetic", Boolean.toString(doc.isSynthetic()));
    }
}
