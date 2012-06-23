package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.MemberDoc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class MemberDocHandler<T extends MemberDoc> extends ProgramElementDocHandler<T> {
// --------------------------- CONSTRUCTORS ---------------------------

    MemberDocHandler() {
        super((Class<T>) MemberDoc.class);
    }

    public MemberDocHandler(final Class<T> docClass) {
        super(docClass);
    }

    public MemberDocHandler(final Class<T> docClass, final ObjectHandlerFilter<Tag> tagFilter) {
        super(docClass, tagFilter);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.removeAttributes(QUALIFIED_NAME);

        el.setAttributes("synthetic", Boolean.toString(doc.isSynthetic()));
    }
}
