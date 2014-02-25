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

package info.mikaelsvensson.devtools.doclet.xml.documentcreator;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreator;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorException;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.extensivedocumentcreator.JavadocItemHandlerException;
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
        } catch (JavadocItemHandlerException e) {
            throw new DocumentCreatorException(e);
        }
    }
    protected void addComment(ElementWrapper parentEl, Tag doc) throws DocumentCreatorException {
        try {
            parentEl.addCommentChild(doc);
        } catch (JavadocItemHandlerException e) {
            throw new DocumentCreatorException(e);
        }
    }
}
