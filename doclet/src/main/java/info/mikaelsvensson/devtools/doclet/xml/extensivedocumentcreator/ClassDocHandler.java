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

import com.sun.javadoc.ClassDoc;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

class ClassDocHandler<T extends ClassDoc> extends ProgramElementDocHandler<T> {
    public static final char FILTER_TYPE_SUPER_CLASS = 's';
    public static final char FILTER_TYPE_CONSTRUCTORS = 'c';
    public static final char FILTER_TYPE_ENUM_CONSTANTS = 'e';
    public static final char FILTER_TYPE_FIELDS = 'f';
    public static final char FILTER_TYPE_INNER_CLASSES = 'n';
    public static final char FILTER_TYPE_INTERFACES = 'i';
    public static final char FILTER_TYPE_METHODS = 'm';
    public static final char FILTER_TYPE_TYPE_PARAMETERS = 't';
    public static final char FILTER_TYPE_TYPE_PARAMETER_TAG = 'p';

    public static final String DEFAULT_ENUM_MEMBER_TYPE_FILTER = "" +
            FILTER_TYPE_ENUM_CONSTANTS +
            FILTER_TYPE_FIELDS +
            FILTER_TYPE_INNER_CLASSES +
            FILTER_TYPE_INTERFACES +
            FILTER_TYPE_METHODS;
    public static final String DEFAULT_INTERFACE_MEMBER_TYPE_FILTER = "" +
            FILTER_TYPE_SUPER_CLASS +
            FILTER_TYPE_FIELDS +
            FILTER_TYPE_INNER_CLASSES +
            FILTER_TYPE_INTERFACES +
            FILTER_TYPE_METHODS +
            FILTER_TYPE_TYPE_PARAMETERS +
            FILTER_TYPE_TYPE_PARAMETER_TAG;
    public static final String DEFAULT_ANNOTATION_MEMBER_TYPE_FILTER = "" +
            FILTER_TYPE_SUPER_CLASS +
            FILTER_TYPE_METHODS;
    public static final String DEFAULT_CLASS_MEMBER_TYPE_FILTER = "" +
            FILTER_TYPE_SUPER_CLASS +
            FILTER_TYPE_CONSTRUCTORS +
            FILTER_TYPE_FIELDS +
            FILTER_TYPE_INNER_CLASSES +
            FILTER_TYPE_INTERFACES +
            FILTER_TYPE_METHODS +
            FILTER_TYPE_TYPE_PARAMETERS +
            FILTER_TYPE_TYPE_PARAMETER_TAG;
    // --------------------------- CONSTRUCTORS ---------------------------

    ClassDocHandler(final Dispatcher dispatcher) {
        this((Class<T>) ClassDoc.class, dispatcher);
    }

    protected ClassDocHandler(final Class<T> cls, final Dispatcher dispatcher) {
        super(cls, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        if (!isClassElement(el)) {
            setTypeAttributes(el, doc);
            el.setAttribute(ELEMENT_QUALIFIED_NAME, doc.qualifiedName());
        } else {
            super.handleImpl(el, doc);

            el.setAttributes(
                    "abstract", Boolean.toString(doc.isAbstract()),
                    "externalizable", Boolean.toString(doc.isExternalizable()),
                    "serializable", Boolean.toString(doc.isSerializable()));

            String classMemberTypeFilter;
            ClassType classType = ClassType.valueOf(doc);
            switch (classType) {
                case ENUM:
                    classMemberTypeFilter = getProperty(ExtensiveDocumentCreator.ENUM_MEMBER_TYPE_FILTER, DEFAULT_ENUM_MEMBER_TYPE_FILTER);
                    break;
                case INTERFACE:
                    classMemberTypeFilter = getProperty(ExtensiveDocumentCreator.INTERFACE_MEMBER_TYPE_FILTER, DEFAULT_INTERFACE_MEMBER_TYPE_FILTER);
                    break;
                case ANNOTATION:
                    classMemberTypeFilter = getProperty(ExtensiveDocumentCreator.ANNOTATION_MEMBER_TYPE_FILTER, DEFAULT_ANNOTATION_MEMBER_TYPE_FILTER);
                    break;
                default:
                    classMemberTypeFilter = getProperty(ExtensiveDocumentCreator.CLASS_MEMBER_TYPE_FILTER, DEFAULT_CLASS_MEMBER_TYPE_FILTER);
                    break;
            }
            el.setAttribute("type", classType.name().toLowerCase());

            for (char c : classMemberTypeFilter.toCharArray()) {
                switch (c) {
                    case FILTER_TYPE_SUPER_CLASS:
                        handleDocImpl(el, "superclass", doc.superclassType());
                        break;
                    case FILTER_TYPE_CONSTRUCTORS:
                        handleDocImpl(el, doc.constructors(), "constructors", "constructor");
                        break;
                    case FILTER_TYPE_ENUM_CONSTANTS:
                        handleDocImpl(el, doc.enumConstants(), "enum-constants", "enum-constant");
                        break;
                    case FILTER_TYPE_FIELDS:
                        handleDocImpl(el, doc.fields(), "fields", "field");
                        break;
                    case FILTER_TYPE_INNER_CLASSES:
                        handleDocImpl(el, doc.innerClasses(), "inner-classes", "inner-class");
                        break;
                    case FILTER_TYPE_INTERFACES:
                        handleDocImpl(el, doc.interfaceTypes(), "interfaces", "interface");
                        break;
                    case FILTER_TYPE_METHODS:
                        handleDocImpl(el, doc.methods(), "methods", "method");
                        break;
                    case FILTER_TYPE_TYPE_PARAMETERS:
                        handleDocImpl(el, doc.typeParameters(), "type-parameters", "type-parameter");
                        break;
                    case FILTER_TYPE_TYPE_PARAMETER_TAG:
                        handleDocImpl(el, doc.typeParamTags(), "type-parameter-tags", "type-parameter-tag");
                        break;
                }
            }
        }
    }

    protected boolean isClassElement(final ElementWrapper el) {
        return el.getTagName().equals(CLASS);
    }

// -------------------------- ENUMERATIONS --------------------------

    enum ClassType {
        ENUM,
        INTERFACE,
        CLASS,
        ANNOTATION;

        static ClassType valueOf(ClassDoc doc) {
            if (doc.isEnum()) {
                return ENUM;
            } else if (doc.isInterface()) {
                return INTERFACE;
            } else if (doc.isAnnotationType()) {
                return ANNOTATION;
            } else {
                return CLASS;
            }
        }
    }
}
