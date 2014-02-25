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

package info.mikaelsvensson.devtools.doclet.xml.documentcreator.extensive;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorException;
import info.mikaelsvensson.devtools.doclet.shared.DocumentWrapper;
import info.mikaelsvensson.devtools.doclet.shared.propertyset.PropertySet;
import info.mikaelsvensson.devtools.doclet.xml.FormatProperty;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.AbstractDocumentCreator;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class ExtensiveDocumentCreator extends AbstractDocumentCreator {
// ------------------------------ FIELDS ------------------------------

    @FormatProperty
    public static final String EXCLUDE_PACKAGE = "excludePackage";

    /**
     * Specifies which types of members should be included in the generated XML document for
     * <em>regular classes</em>, i.e note interfaces or enumerations.
     * <p/>
     * The property value is a string of character where each character represents a type of
     * member.
     * <dl>
     * <dt>s</dt>
     * <dd>Super classes</dd>
     * <dt>c</dt>
     * <dd>Constructors</dd>
     * <dt>e</dt>
     * <dd>Enum constants</dd>
     * <dt>f</dt>
     * <dd>Fields</dd>
     * <dt>n</dt>
     * <dd>Inner classes</dd>
     * <dt>i</dt>
     * <dd>Interfaces</dd>
     * <dt>m</dt>
     * <dd>Methods</dd>
     * <dt>t</dt>
     * <dd>Type parameters</dd>
     * <dt>p</dt>
     * <dd>Type parameter tags</dd>
     * </dl>
     */
    @FormatProperty(defaultValue = ClassDocHandler.DEFAULT_CLASS_MEMBER_TYPE_FILTER)
    public static final String CLASS_MEMBER_TYPE_FILTER = "classMemberTypeFilter";

    /**
     * Specifies which types of members should be included in the generated XML document for
     * <em>enum classes</em>.
     * <p/>
     * The value uses the same syntax as {@link #CLASS_MEMBER_TYPE_FILTER}.
     *
     * @see #CLASS_MEMBER_TYPE_FILTER
     */
    @FormatProperty(defaultValue = ClassDocHandler.DEFAULT_ENUM_MEMBER_TYPE_FILTER)
    public static final String ENUM_MEMBER_TYPE_FILTER = "enumMemberTypeFilter";
    /**
     * Specifies which types of members should be included in the generated XML document for
     * <em>interfaces</em>.
     * <p/>
     * The value uses the same syntax as {@link #CLASS_MEMBER_TYPE_FILTER}.
     *
     * @see #CLASS_MEMBER_TYPE_FILTER
     */
    @FormatProperty(defaultValue = ClassDocHandler.DEFAULT_INTERFACE_MEMBER_TYPE_FILTER)
    public static final String INTERFACE_MEMBER_TYPE_FILTER = "interfaceMemberTypeFilter";
    /**
     * Specifies which types of members should be included in the generated XML document for
     * <em>annotation classes</em>, i.e. interfaces that define annotations.
     * <p/>
     * The value uses the same syntax as {@link #CLASS_MEMBER_TYPE_FILTER}.
     *
     * @see #CLASS_MEMBER_TYPE_FILTER
     */
    @FormatProperty(defaultValue = ClassDocHandler.DEFAULT_ANNOTATION_MEMBER_TYPE_FILTER)
    public static final String ANNOTATION_MEMBER_TYPE_FILTER = "annotationMemberTypeFilter";

    public static final String NAME = "extensive";

    /**
     * Sets whether or not annotations for methods, classes, fields etcetera should be included in
     * the XML document.
     * <p/>
     * This setting does not affect whether or not annotation definitions, i.e. annotation classes,
     * are included in the generated XML document.
     */
    @FormatProperty(defaultValue = "false")
    public static final String SHOW_ANNOTATIONS = "showAnnotations";

    /**
     * Sets whether or not child elements of a particular type should be "wrapped" in an "container
     * elements".
     *
     * If this option is set to {@code true} each {@code <class>} elements will have a child
     * element {@code <methods>} which will contain one {@code <method>} element for each
     * method in the class. Otherwise, i.e. if this configuration property is set to {@code
     * false}, the {@code <method>} elements will be added as direct children of the {@code
     * <class>} element.
     */
    @FormatProperty
    public static final String WRAP_LIST_ELEMENTS = "wrapListElements";

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface DocumentCreator ---------------------

    @Override
    public Document generateDocument(final RootDoc doc, final PropertySet properties) throws DocumentCreatorException {
        DocumentWrapper dw;
        try {
            dw = new DocumentWrapper(createDocument("documentation"));
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException(e);
        }

        Dispatcher dispatcher = new Dispatcher(properties);
        try {
            dispatcher.dispatch(dw, "java", doc);
        } catch (JavadocItemHandlerException e) {
            throw new DocumentCreatorException("Could not parse/process Javadoc: " + e.getMessage(), e);
        }

        return dw.getDocument();
    }
}
