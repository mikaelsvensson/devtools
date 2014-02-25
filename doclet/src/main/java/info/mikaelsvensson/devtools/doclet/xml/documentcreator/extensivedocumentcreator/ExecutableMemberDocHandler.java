/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.doclet.xml.documentcreator.extensivedocumentcreator;

import com.sun.javadoc.*;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

class ExecutableMemberDocHandler<T extends ExecutableMemberDoc> extends MemberDocHandler<T> {
// ------------------------------ FIELDS ------------------------------

    private static final ObjectHandlerFilter<Tag> IGNORE_PARAM_AND_THROWS_TAGS = new NoParamAndThrowsTags();

// --------------------------- CONSTRUCTORS ---------------------------

    ExecutableMemberDocHandler(final Dispatcher dispatcher) {
        this((Class<T>) ExecutableMemberDoc.class, dispatcher);
    }

    public ExecutableMemberDocHandler(final Class<T> docClass, final Dispatcher dispatcher) {
        this(docClass, IGNORE_PARAM_AND_THROWS_TAGS, dispatcher);
    }

    public ExecutableMemberDocHandler(final Class<T> docClass, final ObjectHandlerFilter<Tag> tagFilter, final Dispatcher dispatcher) {
        super(docClass, tagFilter, dispatcher);
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
        parameterEl.addCommentChild(paramTag);
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
