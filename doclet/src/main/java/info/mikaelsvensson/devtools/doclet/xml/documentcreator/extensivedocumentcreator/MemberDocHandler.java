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

import com.sun.javadoc.MemberDoc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

class MemberDocHandler<T extends MemberDoc> extends ProgramElementDocHandler<T> {
// --------------------------- CONSTRUCTORS ---------------------------

    MemberDocHandler(final Dispatcher dispatcher) {
        super((Class<T>) MemberDoc.class, dispatcher);
    }

    public MemberDocHandler(final Class<T> docClass, final Dispatcher dispatcher) {
        super(docClass, dispatcher);
    }

    public MemberDocHandler(final Class<T> docClass, final ObjectHandlerFilter<Tag> tagFilter, final Dispatcher dispatcher) {
        super(docClass, tagFilter, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.removeAttributes(ELEMENT_QUALIFIED_NAME);

        el.setAttributes("synthetic", Boolean.toString(doc.isSynthetic()));
    }
}
