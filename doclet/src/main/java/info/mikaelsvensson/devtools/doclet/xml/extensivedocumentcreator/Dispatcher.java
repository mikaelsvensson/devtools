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

import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;
import info.mikaelsvensson.devtools.doclet.shared.propertyset.PropertySet;

public class Dispatcher {
// ------------------------------ FIELDS ------------------------------

    private PropertySet propertySet;

    private Handler[] handlers;

    private RootDocHandler rootDocHandler;

// --------------------------- CONSTRUCTORS ---------------------------

    public Dispatcher(final PropertySet propertySet) {
        this.propertySet = propertySet;

        rootDocHandler = new RootDocHandler(this);
        handlers = new Handler[] {
                /**/ new AnnotationTypeElementDocHandler(this),
                /*  */ new MethodDocHandler(this),
                /*  */ new ConstructorDocHandler(this),
                /*    */ new ExecutableMemberDocHandler(this),
                /*    */ new FieldDocHandler(this),
                /*      */ new MemberDocHandler(this),
                /*    */ new AnnotationTypeDocHandler(this),
                /*      */ new ClassDocHandler(this),
                /*        */ new ProgramElementDocHandler(this),
                /*        */ rootDocHandler,
                /*        */ new PackageDocHandler(this),
                /*          */ new DocHandler(this),

                /**/ new ParameterHandler(this),

                /**/ new ParamTagHandler(this),
                /**/ new SeeTagHandler(this),
                /**/ new ThrowsTagHandler(this),
                /**/ new SerialFieldTagHandler(this),
                /*  */ new TagHandler(this),

                /**/ new ParameterizedTypeHandler(this),
                /**/ new TypeVariableHandler(this),
                /**/ new WildcardTypeHandler(this),
                /*  */ new TypeHandler(this),

                /**/ new AnnotationDescHandler(this),
                /**/ new AnnotationDescElementValuePairHandler(this),
                /**/ new AnnotationValueHandler(this)
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
                    if (handler.handle(child, javadocObject)) {
                        break;
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
