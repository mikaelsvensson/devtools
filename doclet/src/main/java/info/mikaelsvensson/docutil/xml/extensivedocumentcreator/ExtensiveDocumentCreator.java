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

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.docutil.shared.DocumentCreatorException;
import info.mikaelsvensson.docutil.shared.DocumentWrapper;
import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;
import info.mikaelsvensson.docutil.xml.FormatProperty;
import info.mikaelsvensson.docutil.xml.documentcreator.AbstractDocumentCreator;
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
        DocumentWrapper dw = null;
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
