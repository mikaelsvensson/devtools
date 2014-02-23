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

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorException;
import info.mikaelsvensson.devtools.doclet.shared.DocumentWrapper;
import info.mikaelsvensson.devtools.doclet.shared.propertyset.PropertySet;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class ElementsOnlyDocumentCreator extends AbstractDocumentCreator {
    public static final String NAME = "elementsonly";

/*
    public ElementsOnlyDocumentCreator() throws ParserConfigurationException {
        super();
    }
*/

    @Override
    public Document generateDocument(final RootDoc doc, final PropertySet properties) throws DocumentCreatorException {

        DocumentWrapper documentWrapper;
        try {
            documentWrapper = new DocumentWrapper(createDocument("classes"));
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException(e);
        }

        for (ClassDoc classDoc : doc.classes()) {
            documentWrapper.addChild("class").addChild("name").setText(classDoc.qualifiedName());
        }
        return documentWrapper.getDocument();
    }
}
