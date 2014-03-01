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

package demo;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorException;
import info.mikaelsvensson.devtools.doclet.shared.propertyset.PropertySet;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.AbstractDocumentCreator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;

public class ClassNameOnlyDocumentGenerator extends AbstractDocumentCreator {
    public ClassNameOnlyDocumentGenerator() {
        super("classnameonly");
    }

    @Override
    public Document generateDocument(RootDoc doc, PropertySet properties) throws DocumentCreatorException {
        try {
            Document root = createDocument("classes");
            for (ClassDoc classDoc : doc.classes()) {
                Element clsElement = root.createElement("class");
                clsElement.setAttribute("name", classDoc.name());
                root.getDocumentElement().appendChild(clsElement);
            }
            return root;
        } catch (ParserConfigurationException e) {
            return null;
        }
    }
}
