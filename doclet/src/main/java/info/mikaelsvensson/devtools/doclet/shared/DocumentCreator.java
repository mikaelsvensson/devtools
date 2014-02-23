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

package info.mikaelsvensson.devtools.doclet.shared;

import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.devtools.doclet.shared.propertyset.PropertySet;
import org.w3c.dom.Document;

public interface DocumentCreator {
    /**
     * Creates an XML document based on the supplied Javadoc metadata.
     *
     * @param doc
     *         the root object for the Javadoc documentation.
     * @param properties
     *         command-line properties specified by the user.
     * @return an DOM document. Should never return {@code null} (throws exception instead).
     * @throws DocumentCreatorException
     *         thrown in case an unrecoverable/severe error occurs during document generation.
     */
    Document generateDocument(RootDoc doc, final PropertySet properties) throws DocumentCreatorException;
}
