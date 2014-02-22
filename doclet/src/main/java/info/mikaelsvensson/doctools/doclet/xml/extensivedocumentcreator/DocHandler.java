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

package info.mikaelsvensson.devtools.doclet.xml.extensivedocumentcreator;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

public class DocHandler<T extends Doc> extends Handler<T> {
// ------------------------------ FIELDS ------------------------------

    protected static final String NAME = "name";

    private ObjectHandlerFilter<Tag> tagsFilter = ACCEPT_ALL_FILTER;

// --------------------------- CONSTRUCTORS ---------------------------

    public DocHandler(final Dispatcher dispatcher) {
        super((Class<T>) Doc.class, dispatcher);
    }

    DocHandler(final Class<T> handledClass, final Dispatcher dispatcher) {
        super(handledClass, dispatcher);
    }

    public DocHandler(final Class<T> docClass, final ObjectHandlerFilter<Tag> tagFilter, final Dispatcher dispatcher) {
        super(docClass, dispatcher);
        this.tagsFilter = tagFilter;
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        String text = doc.commentText();
        if (null != text && text.length() > 0) {
            el.addCommentChild(doc);
        }
        el.setAttribute(NAME, doc.name());

        handleDocImpl(el, doc.tags(), tagsFilter, "tags", "tag");
    }
}
