package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.*;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ExecutableMemberDocHandler<T extends ExecutableMemberDoc> extends MemberDocHandler<T> {

    ExecutableMemberDocHandler() {
        super((Class<T>) ExecutableMemberDoc.class);
    }

    public ExecutableMemberDocHandler(final Class<T> docClass) {
        super(docClass);
    }

    @Override
    void handleImpl(final ElementWrapper el, final ExecutableMemberDoc doc) {
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

    private void handleParameters(final ElementWrapper el, final Parameter[] parameters, final ParamTag[] paramTags) {
        if (parameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("parameters");
            for (Parameter parameter : parameters) {
                ElementWrapper parameterEl = handleDocImpl(parametersEl, parameter, "parameter");
                for (ParamTag paramTag : paramTags) {
                    if (paramTag.parameterName().equals(parameter.name())) {
                        ElementWrapper paramTagEl = handleDocImpl(parameterEl, paramTag, "tag");
                        paramTagEl.remoteAttributes("kind", "parameter-name");
                    }
                }
            }
        }
    }
    private void handleThrows(final ElementWrapper el, final Type[] parameters, final ThrowsTag[] paramTags) {
        if (parameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("throws-list");
            for (Type parameter : parameters) {
                ElementWrapper parameterEl = handleDocImpl(parametersEl, parameter, "throws");
                for (ThrowsTag paramTag : paramTags) {
                    if (paramTag.exceptionType().qualifiedTypeName().equals(parameter.qualifiedTypeName())) {
                        ElementWrapper paramTagEl = handleDocImpl(parameterEl, paramTag, "tag");
                        paramTagEl.remoteAttributes("kind");
                    }
                }
            }
        }
    }
    private void handleTypeParameters(final ElementWrapper el, final Type[] parameters, final ParamTag[] paramTags) {
        if (parameters.length > 0) {
            ElementWrapper parametersEl = el.addChild("throws-list");
            for (Type parameter : parameters) {
                ElementWrapper parameterEl = handleDocImpl(parametersEl, parameter, "throws");
                for (ParamTag paramTag : paramTags) {
                    if (paramTag.parameterName().equals(parameter.qualifiedTypeName())) {
                        ElementWrapper paramTagEl = handleDocImpl(parameterEl, paramTag, "tag");
                        paramTagEl.remoteAttributes("kind");
                    }
                }
            }
        }
    }
}
