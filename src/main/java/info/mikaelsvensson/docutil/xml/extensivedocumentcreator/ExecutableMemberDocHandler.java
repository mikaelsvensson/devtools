package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.*;
import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.commenttext.InlineTagHandlerException;

class ExecutableMemberDocHandler<T extends ExecutableMemberDoc> extends MemberDocHandler<T> {
// ------------------------------ FIELDS ------------------------------

    private static final ObjectHandlerFilter<Tag> IGNORE_PARAM_AND_THROWS_TAGS = new NoParamAndThrowsTags();

// --------------------------- CONSTRUCTORS ---------------------------

    ExecutableMemberDocHandler() {
        this((Class<T>) ExecutableMemberDoc.class);
    }

    public ExecutableMemberDocHandler(final Class<T> docClass) {
        this(docClass, IGNORE_PARAM_AND_THROWS_TAGS);
    }

    public ExecutableMemberDocHandler(final Class<T> docClass, final ObjectHandlerFilter<Tag> tagFilter) {
        super(docClass, tagFilter);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                "native", Boolean.toString(doc.isNative()),
                "synchronized", Boolean.toString(doc.isSynchronized()),
                "varargs", Boolean.toString(doc.isVarArgs()));

        handleParameters(el, doc.parameters(), doc.paramTags());

        handleThrows(el, doc.thrownExceptionTypes(), doc.throwsTags());

        handleTypeParameters(el, doc.typeParameters(), doc.typeParamTags());
    }

    private void handleParameters(final ElementWrapper el, final Parameter[] parameters, final ParamTag[] paramTags) throws JavadocItemHandlerException {
        if (parameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("parameters");
            for (Parameter parameter : parameters) {
                ElementWrapper parameterEl = handleDocImpl(parametersEl, parameter, "parameter");
                for (ParamTag paramTag : paramTags) {
                    if (paramTag.parameterName().equals(parameter.name())) {
                        addComment(parameterEl, paramTag);
                    }
                }
            }
        }
    }

    protected void addComment(final ElementWrapper parameterEl, final Tag paramTag) throws JavadocItemHandlerException {
        try {
            parameterEl.addCommentChild(paramTag);
        } catch (InlineTagHandlerException e) {
            throw new JavadocItemHandlerException("Could not parse/process one of the Javadoc tags. ", e);
        }
    }

    private void handleThrows(final ElementWrapper el, final Type[] exceptionTypes, final ThrowsTag[] throwsTags) throws JavadocItemHandlerException {
        if (exceptionTypes.length > 0) {
            ElementWrapper throwsListEl = el.addChild("throws-list");
            for (Type exceptionType : exceptionTypes) {
                ElementWrapper throwsEl = handleDocImpl(throwsListEl, exceptionType, "throws");
                for (ThrowsTag throwsTag : throwsTags) {
                    if (throwsTag.exceptionType().qualifiedTypeName().equals(exceptionType.qualifiedTypeName())) {
                        addComment(throwsEl, throwsTag);
                    }
                }
            }
        }
    }

    private void handleTypeParameters(final ElementWrapper el, final Type[] typeParameters, final ParamTag[] typeParamTags) throws JavadocItemHandlerException {
        if (typeParameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("type-parameters");
            for (Type typeParameter : typeParameters) {
                ElementWrapper typeParameterEl = handleDocImpl(parametersEl, typeParameter, "type-parameter");
                for (ParamTag typeParamTag : typeParamTags) {
                    if (typeParamTag.parameterName().equals(typeParameter.qualifiedTypeName())) {
                        addComment(typeParameterEl, typeParamTag);
                    }
                }
            }
        }
    }

// -------------------------- INNER CLASSES --------------------------

    protected static class NoParamAndThrowsTags implements ObjectHandlerFilter<Tag> {
        @Override
        public boolean accept(final Tag object) {
            return !(object instanceof ParamTag || object instanceof ThrowsTag);
        }
    }
}
