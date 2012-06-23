package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class MethodDocHandler<T extends MethodDoc> extends ExecutableMemberDocHandler<T> {
// ------------------------------ FIELDS ------------------------------

    private static final ObjectHandlerFilter<Tag> IGNORE_RETURN_AND_PARAM_AND_THROWS_TAGS = new NoReturnTag();

// --------------------------- CONSTRUCTORS ---------------------------

    MethodDocHandler() {
        this((Class<T>) MethodDoc.class);
    }

    public MethodDocHandler(final Class<T> docClass) {
        super(docClass, IGNORE_RETURN_AND_PARAM_AND_THROWS_TAGS);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes("abstract", Boolean.toString(doc.isAbstract()));

        handleReturn(el, doc.returnType(), doc.tags("@return"));

        handleDocImpl(el, "overrides", doc.overriddenType());
    }

    private void handleReturn(final ElementWrapper el, final Type returnType, final Tag[] paramTags) throws JavadocItemHandlerException {
        ElementWrapper parameterEl = handleDocImpl(el, returnType, "returns");
        for (Tag paramTag : paramTags) {
            addComment(parameterEl, paramTag);
        }
    }

// -------------------------- INNER CLASSES --------------------------

    private static class NoReturnTag extends NoParamAndThrowsTags {
        @Override
        public boolean accept(final Tag object) {
            return !(object.name().equals("@return")) && super.accept(object);
        }
    }
}
