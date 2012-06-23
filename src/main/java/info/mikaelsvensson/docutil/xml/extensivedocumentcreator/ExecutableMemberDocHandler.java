package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.*;
import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.commenttext.InlineTagHandlerException;

class ExecutableMemberDocHandler<T extends ExecutableMemberDoc> extends MemberDocHandler<T> {

    private static final ObjectHandlerFilter<Tag> IGNORE_PARAM_AND_THROWS_TAGS = new ObjectHandlerFilter<Tag>() {
        @Override
        public boolean accept(final Tag object) {
            return !(object instanceof ParamTag || object instanceof ThrowsTag);
        }
    };

    ExecutableMemberDocHandler() {
        this((Class<T>) ExecutableMemberDoc.class);
    }

    public ExecutableMemberDocHandler(final Class<T> docClass) {
        super(docClass);
        tagsFilter = IGNORE_PARAM_AND_THROWS_TAGS;
    }

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                "native", Boolean.toString(doc.isNative()),
                "synchronized", Boolean.toString(doc.isSynchronized()),
                "varargs", Boolean.toString(doc.isVarArgs()));

        handleParameters(el, doc.parameters(), doc.paramTags());

//        handleDocImpl(el, parameters, "parameters", "parameter");

//        handleDocImpl(el, paramTags, "parameter-tags", "parameter-tag");

        handleThrows(el, doc.thrownExceptionTypes(), doc.throwsTags());

//        handleDocImpl(el, doc.thrownExceptionTypes(), "thrown-exceptions", "thrown-exception");

//        handleDocImpl(el, doc.throwsTags(), "throws-tags", "throws-tags");

        handleTypeParameters(el, doc.typeParameters(), doc.typeParamTags());

//        handleDocImpl(el, doc.typeParameters(), "type-parameters", "type-parameter");

//        handleDocImpl(el, doc.typeParamTags(), "type-parameter-tags", "type-parameter-tag");
    }

    private void handleParameters(final ElementWrapper el, final Parameter[] parameters, final ParamTag[] paramTags) throws JavadocItemHandlerException {
        if (parameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("parameters");
            for (Parameter parameter : parameters) {
                ElementWrapper parameterEl = handleDocImpl(parametersEl, parameter, "parameter");
                for (ParamTag paramTag : paramTags) {
                    if (paramTag.parameterName().equals(parameter.name())) {
                        try {
                            parameterEl.addCommentChild(paramTag);
                        } catch (InlineTagHandlerException e) {
                            throw new JavadocItemHandlerException("Could not parse/process one of the Javadoc tags. ", e);
                        }
//                        ElementWrapper paramTagEl = handleDocImpl(parameterEl, paramTag, "tag");
//                        paramTagEl.remoteAttributes("kind", "parameter-name");
                    }
                }
            }
        }
    }

    private void handleThrows(final ElementWrapper el, final Type[] parameters, final ThrowsTag[] paramTags) throws JavadocItemHandlerException {
        if (parameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("throws-list");
            for (Type parameter : parameters) {
                ElementWrapper parameterEl = handleDocImpl(parametersEl, parameter, "throws");
                for (ThrowsTag paramTag : paramTags) {
                    if (paramTag.exceptionType().qualifiedTypeName().equals(parameter.qualifiedTypeName())) {
                        try {
                            parameterEl.addCommentChild(paramTag);
                        } catch (InlineTagHandlerException e) {
                            throw new JavadocItemHandlerException("Could not parse/process one of the Javadoc tags. ", e);
                        }
//                        ElementWrapper paramTagEl = handleDocImpl(parameterEl, paramTag, "tag");
//                        paramTagEl.remoteAttributes("kind");
                    }
                }
            }
        }
    }

    private void handleTypeParameters(final ElementWrapper el, final Type[] parameters, final ParamTag[] paramTags) throws JavadocItemHandlerException {
        if (parameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("throws-list");
            for (Type parameter : parameters) {
                ElementWrapper parameterEl = handleDocImpl(parametersEl, parameter, "throws");
                for (ParamTag paramTag : paramTags) {
                    if (paramTag.parameterName().equals(parameter.qualifiedTypeName())) {
                        try {
                            parameterEl.addCommentChild(paramTag);
                        } catch (InlineTagHandlerException e) {
                            throw new JavadocItemHandlerException("Could not parse/process one of the Javadoc tags. ", e);
                        }
//                        ElementWrapper paramTagEl = handleDocImpl(parameterEl, paramTag, "tag");
//                        paramTagEl.remoteAttributes("kind");
                    }
                }
            }
        }
    }
}
