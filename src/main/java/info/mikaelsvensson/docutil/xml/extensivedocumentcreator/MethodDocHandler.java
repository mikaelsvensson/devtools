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

package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class MethodDocHandler<T extends MethodDoc> extends ExecutableMemberDocHandler<T> {
// ------------------------------ FIELDS ------------------------------

    private static final ObjectHandlerFilter<Tag> IGNORE_RETURN_AND_PARAM_AND_THROWS_TAGS = new NoReturnTag();

// --------------------------- CONSTRUCTORS ---------------------------

    MethodDocHandler(final Dispatcher dispatcher) {
        this((Class<T>) MethodDoc.class, dispatcher);
    }

    public MethodDocHandler(final Class<T> docClass, final Dispatcher dispatcher) {
        super(docClass, IGNORE_RETURN_AND_PARAM_AND_THROWS_TAGS, dispatcher);
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
        ElementWrapper parameterEl = handleDocImpl(el, returnType, "return-type");
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
