package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.MemberDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class MemberDocHandler<T extends MemberDoc> extends ProgramElementDocHandler<T> {

    MemberDocHandler() {
        super((Class<T>) MemberDoc.class);
    }

    public MemberDocHandler(final Class<T> docClass) {
        super(docClass);
    }

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes("synthetic", Boolean.toString(doc.isSynthetic()));
    }
}
