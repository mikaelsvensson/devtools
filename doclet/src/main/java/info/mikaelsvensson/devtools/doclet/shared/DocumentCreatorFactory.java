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

import info.mikaelsvensson.devtools.doclet.db2.Db2MetadataDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.EnumDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.StandardDocumentCreator;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.extensivedocumentcreator.ExtensiveDocumentCreator;

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
