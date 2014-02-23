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

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Type;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

abstract class Handler<T> {
// ------------------------------ FIELDS ------------------------------

    static final String CLASS = "class";

    protected static final ObjectHandlerFilter ACCEPT_ALL_FILTER = new ObjectHandlerFilter() {
        @Override
        public boolean accept(final Object object) {
            return true;
        }
    };
    private Dispatcher dispatcher;
    private Class<T> handledClass;

// --------------------------- CONSTRUCTORS ---------------------------

    Handler(final Class<T> handledClass, final Dispatcher dispatcher) {
        this.handledClass = handledClass;
        this.dispatcher = dispatcher;
    }

// -------------------------- OTHER METHODS --------------------------

    protected String getProperty(final String property) {
        return getProperty(property, null);
    }

    boolean handle(final ElementWrapper el, final Object javadocObject) throws JavadocItemHandlerException {
        if (handledClass.isAssignableFrom(javadocObject.getClass())) {
            handleImpl(el, (T) javadocObject);
            return true;
        } else {
            return false;
        }
    }

    void handleImpl(final ElementWrapper el, T doc) throws JavadocItemHandlerException {
    }

    protected <X> ElementWrapper handleDocImpl(final ElementWrapper el, final String elName, final X javadocObject) throws JavadocItemHandlerException {
        return dispatcher.dispatch(el, elName, javadocObject);
    }

    protected void handleDocImpl(final ElementWrapper el, final Object[] javadocObjects, final String listElName, final String elName) throws JavadocItemHandlerException {
        handleDocImpl(el, javadocObjects, ACCEPT_ALL_FILTER, listElName, elName);
    }

    protected <X> ElementWrapper handleDocImpl(final ElementWrapper el, final String elName, final X javadocObject, final boolean includeClassHandler) throws JavadocItemHandlerException {
        return dispatcher.dispatch(el, elName, javadocObject, includeClassHandler);
    }

    protected void handleDocImpl(final ElementWrapper el, final Object[] javadocObjects, final String listElName, final String elName, final boolean includeClassHandler) throws JavadocItemHandlerException {
        handleDocImpl(el, javadocObjects, ACCEPT_ALL_FILTER, listElName, elName, includeClassHandler);
    }

    protected <X> void handleDocImpl(final ElementWrapper el, final X[] javadocObjects, final ObjectHandlerFilter<X> filter, final String listElName, final String elName) throws JavadocItemHandlerException {
        handleDocImpl(el, javadocObjects, filter, listElName, elName, false);
    }

    protected <X> void handleDocImpl(ElementWrapper el, final X[] javadocObjects, final ObjectHandlerFilter<X> filter, final String listElName, final String elName, final boolean includeClassHandler) throws JavadocItemHandlerException {
        if (null != javadocObjects && javadocObjects.length > 0) {
            if (getBooleanProperty(ExtensiveDocumentCreator.WRAP_LIST_ELEMENTS, true)) {
                el = el.addChild(listElName);
            }
            for (X javadocObject : javadocObjects) {
                if (filter.accept(javadocObject)) {
                    handleDocImpl(el, elName, javadocObject, includeClassHandler);
                }
            }
        }
    }

    protected boolean getBooleanProperty(final String property, final boolean defaultValue) {
        return Boolean.valueOf(getProperty(property, defaultValue ? Boolean.TRUE.toString() : Boolean.FALSE.toString()));
    }

    protected String getProperty(final String property, final String defaultValue) {
        String value = dispatcher.getProperty(property);
        return value != null ? value : defaultValue;
    }

    protected void handleValue(final ElementWrapper el, final AnnotationValue annotationValue) throws JavadocItemHandlerException {
        Object value = annotationValue.value();
        if (value instanceof Type) {
            el.setAttribute("type", "class-reference");
            handleDocImpl(el, value, "class-reference");
        } else if (value instanceof FieldDoc) {
            el.setAttribute("type", "enum-constant");
            handleDocImpl(el, value, "enum-constant");
        } else if (value instanceof AnnotationDesc) {
            el.setAttribute("type", "single-annotation");
            handleDocImpl(el, value, "single-annotation");
        } else if (value instanceof AnnotationValue[]) {
            el.setAttribute("type", "values");
            for (AnnotationValue v : (AnnotationValue[])value) {
                handleValue(el.addChild("value"), v);
            }
        } else {
            el.setAttribute("type", "single-object");
            el.setText(value.toString());
        }
    }

    protected ElementWrapper handleDocImpl(final ElementWrapper el, final Object javadocObject, final String elName) throws JavadocItemHandlerException {
        if (null != javadocObject) {
            return handleDocImpl(el, elName, javadocObject);
        }
        return null;
    }

    protected void setTypeAttributes(final ElementWrapper el, final Type doc) {
        el.setAttributes(
                "dimension", Integer.toString(getDimensionCount(doc.dimension())),
                "primitive", Boolean.toString(doc.isPrimitive()),
                ProgramElementDocHandler.ELEMENT_QUALIFIED_NAME, doc.qualifiedTypeName());
    }

    protected int getDimensionCount(final String dimension) {
        int count = 0;
        int pos = -1;
        while ((pos = dimension.indexOf('[', pos + 1)) != -1) {
            count++;
        }
        return count;
    }
}
