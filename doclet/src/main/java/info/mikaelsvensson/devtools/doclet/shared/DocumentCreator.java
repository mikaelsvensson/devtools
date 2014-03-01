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

/**
 * A document creator, also known as a formatter, defines how the resulting XML document will be constructed. The
 * document creator takes the Javadoc documentation and processed it into an XML document.
 * <p/>
 * Different document creators offer different features in terms of what information is put into the XML document.
 * Some creators are very verbose and generate large XML documents with tons of information. Others producer compacter
 * output or very specific output for a subset of the documented classes.
 * <p/>
 * Choose a document creator based on how you plan to use the generated XML document, i.e. what kind of information
 * you need extracted from the source code.
 */
public interface DocumentCreator {
    /**
     * Creates an XML document based on the supplied Javadoc metadata.
     *
     * @param doc        the root object for the Javadoc documentation.
     * @param properties command-line properties specified by the user.
     * @return an DOM document. Should never return {@code null} (throws exception instead).
     * @throws DocumentCreatorException thrown in case an unrecoverable/severe error occurs during document generation.
     */
    Document generateDocument(RootDoc doc, final PropertySet properties) throws DocumentCreatorException;

    /**
     * String used to identify the document creator in configuration files. Hence, it must be unique.
     *
     * @return string which uniquely identifies the document creator in lists of other document creator names
     */
    String getName();
}
