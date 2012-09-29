/*
 * Copyright (c) 2012, Mikael Svensson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the copyright holder nor the names of the
 *       contributors of this software may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package info.mikaelsvensson.doctools.xml.extensivedocumentcreator;

import com.sun.javadoc.*;
import info.mikaelsvensson.doctools.shared.ElementWrapper;

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
