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

package info.mikaelsvensson.devtools.doclet.shared;

import info.mikaelsvensson.devtools.doclet.db2.Db2MetadataDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.EnumDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.StandardDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.extensivedocumentcreator.ExtensiveDocumentCreator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class DocumentCreatorFactory {
    private static Map<String, Class<? extends DocumentCreator>> factories = new HashMap<String, Class<? extends DocumentCreator>>();

    static {
        DocumentCreatorFactory.registerDocumentCreatorFactory(EnumDocumentCreator.NAME, EnumDocumentCreator.class);
        DocumentCreatorFactory.registerDocumentCreatorFactory(StandardDocumentCreator.NAME, StandardDocumentCreator.class);
        DocumentCreatorFactory.registerDocumentCreatorFactory(ElementsOnlyDocumentCreator.NAME, ElementsOnlyDocumentCreator.class);
        DocumentCreatorFactory.registerDocumentCreatorFactory(ExtensiveDocumentCreator.NAME, ExtensiveDocumentCreator.class);
        DocumentCreatorFactory.registerDocumentCreatorFactory(Db2MetadataDocumentCreator.NAME, Db2MetadataDocumentCreator.class);
    }

    private DocumentCreatorFactory() {
    }

    public static DocumentCreator getDocumentCreator(String id) {

        if (factories.containsKey(id)) {
            try {
                return factories.get(id).getConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                return null;
            } catch (InvocationTargetException e) {
                return null;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void registerDocumentCreatorFactory(String id, Class<? extends DocumentCreator> documentCreatorClass) {
        factories.put(id, documentCreatorClass);
    }
}
