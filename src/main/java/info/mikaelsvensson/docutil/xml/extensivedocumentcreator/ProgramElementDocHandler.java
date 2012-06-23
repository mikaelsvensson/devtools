package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ProgramElementDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ProgramElementDocHandler<T extends ProgramElementDoc> extends DocHandler<T> {

    ProgramElementDocHandler() {
        super((Class<T>) ProgramElementDoc.class);
    }

    public ProgramElementDocHandler(final Class<T> cls) {
        super(cls);
    }

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
//                    "qualified-name", doc.qualifiedName(),
                "name", doc.name(),
                "final", Boolean.toString(doc.isFinal()),
                "access", getAccess(doc),
                "static", Boolean.toString(doc.isStatic()));

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
