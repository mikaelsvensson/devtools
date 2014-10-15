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

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

public final class DocumentCreatorFactory {

    private static ServiceLoader<DocumentCreator> documentCreatorLoader = ServiceLoader.load(DocumentCreator.class);

    private DocumentCreatorFactory() {
    }

    /**
     * First, the factory finds out if requested document creator name matches any of the known document
     * creators (the ones defined in the META-INF folder according to the ServiceLoader API requirements).
     * <p/>
     * If that fails the factory guesses that the requested document creator name is actually the class
     * name of a DocumentCreator implementation and attempts to load, and instantiate, that class.
     */
    public static DocumentCreator getDocumentCreator(String name) throws DocumentCreatorFactoryException {
        if (name == null || "".equals(name)) {
            throw new DocumentCreatorFactoryException("No document creator specified.");
        }
        /*
         * Scan known document creators.
         */
        for (DocumentCreator creator : documentCreatorLoader) {
            if (creator.getName().equals(name)) {
                return creator;
            }
        }
        /*
         * Test if requested name is actually the name of an implementation class.
         */
        try {
            Class<?> cls = ClassLoader.getSystemClassLoader().loadClass(name);
            return (DocumentCreator) cls.getConstructor().newInstance();
        } catch (ClassCastException e) {
            throw new DocumentCreatorFactoryException(e);
        } catch (NoSuchMethodException e) {
            throw new DocumentCreatorFactoryException(e);
        } catch (InvocationTargetException e) {
            throw new DocumentCreatorFactoryException(e);
        } catch (InstantiationException e) {
            throw new DocumentCreatorFactoryException(e);
        } catch (IllegalAccessException e) {
            throw new DocumentCreatorFactoryException(e);
        } catch (ClassNotFoundException e) {
            throw new DocumentCreatorFactoryException(e);
        }
    }
}
