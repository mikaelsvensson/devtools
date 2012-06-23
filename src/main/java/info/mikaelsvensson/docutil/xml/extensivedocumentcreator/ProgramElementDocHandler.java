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
