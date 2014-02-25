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
