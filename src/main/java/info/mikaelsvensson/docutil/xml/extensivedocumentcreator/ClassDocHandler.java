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

import com.sun.javadoc.ClassDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ClassDocHandler<T extends ClassDoc> extends ProgramElementDocHandler<T> {
// --------------------------- CONSTRUCTORS ---------------------------

    ClassDocHandler(final Dispatcher dispatcher) {
        super((Class<T>) ClassDoc.class, dispatcher);
    }

    protected ClassDocHandler(final Class<T> cls, final Dispatcher dispatcher) {
        super(cls, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                "abstract", Boolean.toString(doc.isAbstract()),
                "externalizable", Boolean.toString(doc.isExternalizable()),
                "serializable", Boolean.toString(doc.isSerializable()));

        handleDocImpl(el, "superclass", doc.superclassType());

        handleDocImpl(el, doc.constructors(), "constructors", "constructor");

        handleDocImpl(el, doc.enumConstants(), "enum-constants", "enum-constant");

        handleDocImpl(el, doc.fields(), "fields", "field");

        handleDocImpl(el, doc.innerClasses(), "inner-classes", "inner-class");

        handleDocImpl(el, doc.interfaceTypes(), "interfaces", "interface");

        handleDocImpl(el, doc.methods(), "methods", "method");

        handleDocImpl(el, doc.typeParameters(), "type-parameters", "type-parameter");

        handleDocImpl(el, doc.typeParamTags(), "type-parameter-tags", "type-parameter-tag");
    }
}
