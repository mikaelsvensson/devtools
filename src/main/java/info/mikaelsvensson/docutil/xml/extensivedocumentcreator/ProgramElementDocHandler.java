package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ProgramElementDocHandler<T extends ProgramElementDoc> extends DocHandler<T> {
// ------------------------------ FIELDS ------------------------------

    protected static final String QUALIFIED_NAME = "qualified-name";
//    protected static final String NAME = "name";
    protected static final String FINAL = "final";
    protected static final String ACCESS = "access";
    protected static final String STATIC = "static";

// --------------------------- CONSTRUCTORS ---------------------------

    ProgramElementDocHandler(final Dispatcher dispatcher) {
        super((Class<T>) ProgramElementDoc.class, dispatcher);
    }

    public ProgramElementDocHandler(final Class<T> cls, final Dispatcher dispatcher) {
        super(cls, dispatcher);
    }

    public ProgramElementDocHandler(final Class<T> docClass, final ObjectHandlerFilter<Tag> tagFilter, final Dispatcher dispatcher) {
        super(docClass, tagFilter, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                QUALIFIED_NAME, doc.qualifiedName(),
//                NAME, doc.name(),
                FINAL, Boolean.toString(doc.isFinal()),
                ACCESS, getAccess(doc),
                STATIC, Boolean.toString(doc.isStatic()));

        handleDocImpl(el, doc.annotations(), "annotations", "annotation");
    }

    private String getAccess(ProgramElementDoc javadocItem) {
        if (javadocItem.isProtected()) {
            return "package";
        } else if (javadocItem.isPrivate()) {
            return "private";
        } else if (javadocItem.isPublic()) {
            return "public";
        } else {
            return "default";
        }
    }
}
