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

package info.mikaelsvensson.docutil.xml.documentcreator;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.DocumentCreator;
import info.mikaelsvensson.docutil.shared.DocumentCreatorException;
import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.commenttext.InlineTagHandlerException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public abstract class AbstractDocumentCreator implements DocumentCreator {
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_Q_NAME = "qualified-name";

    protected Document createDocument(final String rootElementName) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));
        return doc;
    }

    protected void addComment(ElementWrapper parentEl, Doc doc) throws DocumentCreatorException {
        try {
            parentEl.addCommentChild(doc);
        } catch (InlineTagHandlerException e) {
            throw new DocumentCreatorException("Could not parse/process one of the Javadoc tags. ", e);
        }
    }
    protected void addComment(ElementWrapper parentEl, Tag doc) throws DocumentCreatorException {
        try {
            parentEl.addCommentChild(doc);
        } catch (InlineTagHandlerException e) {
            throw new DocumentCreatorException("Could not parse/process one of the Javadoc tags. ", e);
        }
    }
}
