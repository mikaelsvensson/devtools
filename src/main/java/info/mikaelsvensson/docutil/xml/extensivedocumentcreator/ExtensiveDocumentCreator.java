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
import info.mikaelsvensson.docutil.xml.documentcreator.AbstractDocumentCreator;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class ExtensiveDocumentCreator extends AbstractDocumentCreator {
// ------------------------------ FIELDS ------------------------------

    public static final String CLASS_MEMBER_TYPE_FILTER = "classMemberTypeFilter";
    public static final String ENUM_MEMBER_TYPE_FILTER = "enumMemberTypeFilter";
    public static final String EXCLUDE_PACKAGE = "excludePackage";
    public static final String INTERFACE_MEMBER_TYPE_FILTER = "interfaceMemberTypeFilter";
    public static final String NAME = "extensive";
    public static final String SHOW_ANNOTATIONS = "showAnnotations";
    public static final String SIMPLE_TYPE_DATA = "simpleTypeData";
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
            throw new DocumentCreatorException("Could not parse/process Javadoc. ", e);
        }

        return dw.getDocument();
    }
}
