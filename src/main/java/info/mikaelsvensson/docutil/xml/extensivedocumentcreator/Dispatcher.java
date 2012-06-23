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

import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;

public class Dispatcher {
// ------------------------------ FIELDS ------------------------------

    private PropertySet propertySet;

    private Handler[] handlers;

// --------------------------- CONSTRUCTORS ---------------------------

    public Dispatcher(final PropertySet propertySet) {
        this.propertySet = propertySet;
        handlers = new Handler[] {
                /**/ new AnnotationTypeElementDocHandler(this),
                /*  */ new MethodDocHandler(this),
                /*  */ new ConstructorDocHandler(this),
                /*    */ new ExecutableMemberDocHandler(this),
                /*    */ new FieldDocHandler(this),
                /*      */ new MemberDocHandler(this),
                /*      */ new ClassDocHandler(this),
                /*        */ new ProgramElementDocHandler(this),
                /*        */ new RootDocHandler(this),
                /*        */ new PackageDocHandler(this),
                /*          */ new DocHandler(this),

                /**/ new ParameterHandler(this),

                /**/ new ParamTagHandler(this),
                /**/ new SeeTagHandler(this),
                /**/ new ThrowsTagHandler(this),
                /**/ new SerialFieldTagHandler(this),
                /*  */ new TagHandler(this),

                /**/ new AnnotationTypeDocHandler(this),
                /**/ new ParameterizedTypeHandler(this),
                /**/ new TypeVariableHandler(this),
                /**/ new WildcardTypeHandler(this),
                /*  */ new TypeHandler(this)
        };
    }

// -------------------------- OTHER METHODS --------------------------

    public ElementWrapper dispatch(final ElementWrapper el, String elementName, final Object javadocObject) throws JavadocItemHandlerException {
        return dispatch(el, elementName, javadocObject, false);
    }

    public ElementWrapper dispatch(final ElementWrapper el, String elementName, final Object javadocObject, final boolean includeClassHandler) throws JavadocItemHandlerException {
        if (javadocObject != null) {
            ElementWrapper child = el.addChild(elementName);

            for (Handler handler : handlers) {
                if (includeClassHandler || !(handler instanceof ClassDocHandler)) {
                    if (handler.handle(child, javadocObject)) {
                        break;
                    }
                }
            }

            return child;
        }
        return null;
    }

    public String getProperty(final String key) {
        return propertySet.getProperty(key);
    }
}
