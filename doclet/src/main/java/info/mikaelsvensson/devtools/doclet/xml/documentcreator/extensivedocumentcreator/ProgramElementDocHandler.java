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

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

class ProgramElementDocHandler<T extends ProgramElementDoc> extends DocHandler<T> {
// ------------------------------ FIELDS ------------------------------

    public static final String ELEMENT_QUALIFIED_NAME = "qualified-name";
    protected static final String ATTR_FINAL = "final";
    protected static final String ATTR_ACCESS = "access";
    protected static final String ATTR_STATIC = "static";

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
                ELEMENT_QUALIFIED_NAME, doc.qualifiedName(),
                ATTR_FINAL, Boolean.toString(doc.isFinal()),
                ATTR_ACCESS, getAccess(doc),
                ATTR_STATIC, Boolean.toString(doc.isStatic()));
        if (getBooleanProperty(ExtensiveDocumentCreator.SHOW_ANNOTATIONS, false)) {
            AnnotationDesc[] annotations = doc.annotations();
            handleDocImpl(el, annotations, "annotations", "annotation");
        }
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
