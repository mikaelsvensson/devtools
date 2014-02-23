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

package info.mikaelsvensson.devtools.doclet.xml.extensivedocumentcreator;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

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
