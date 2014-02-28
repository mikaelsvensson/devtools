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
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.devtools.doclet.shared.DocumentCreatorException;
import info.mikaelsvensson.devtools.doclet.shared.DocumentWrapper;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;
import info.mikaelsvensson.devtools.doclet.shared.propertyset.PropertySet;
import info.mikaelsvensson.devtools.doclet.xml.FormatProperty;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Highly specialized document creator which <em>only</em> outputs information about {@code enum} classes. On the other
 * hand, it attempts to load each enum constant (using class loader) in order to access their <em>run-time values</em>.
 * <p/>
 * This allows this document creator to access the names, types <em>and values</em> for the enumeration constants.
 */
public class EnumDocumentCreator extends AbstractDocumentCreator {
    public static final String NAME = "enum";

    /**
     * Specified where the compiled classes (*.class files) can be found. The class file folder is used to instantiate
     * enum classes in order to invoke their getters and retrieve their return values.
     */
    @FormatProperty
    public static final String PARAMETER_CLASS_FOLDER = "classfolder";

    @Override
    public Document generateDocument(final RootDoc doc, final PropertySet properties) throws DocumentCreatorException {

        File classFolder = new File(properties.getProperty(PARAMETER_CLASS_FOLDER));

        URLClassLoader loader = null;
        try {
            URL url = classFolder.toURI().toURL();
            loader = new URLClassLoader(new URL[]{url});
        } catch (MalformedURLException e) {
            doc.printWarning("Could not create URL class loader using (perhaps '" + classFolder.getAbsolutePath() + "' is not the correct the path to the class folder?). Detailed information about enum constants will not be available.");
        }

        DocumentWrapper docEl;
        try {
            docEl = new DocumentWrapper(createDocument("enumerations"));
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException(e);
        }
        for (ClassDoc classDoc : doc.classes()) {
            if (classDoc.isEnum()) {
                ElementWrapper enumEl = docEl.addChild("enum",
                        "name", classDoc.name(),
                        "qualified-name", classDoc.qualifiedName());

                ElementWrapper interfacesEl = enumEl.addChild("interfaces");
                for (ClassDoc implInterface : classDoc.interfaces()) {
                    interfacesEl.addChild("interface",
                            "name", implInterface.name(),
                            "qualified-name", implInterface.qualifiedName());
                }

                for (FieldDoc enumField : classDoc.enumConstants()) {
                    ElementWrapper constantEl = enumEl.addChild("constant", "name", enumField.name());

                    if (loader != null) {
                        Map<String, String> enumConstantProperties = getEnumConstantProperties(doc, loader, enumField);
                        for (Map.Entry<String, String> entry : enumConstantProperties.entrySet()) {
                            constantEl.addChild("property", "name", entry.getKey(), "value", entry.getValue());
                        }
                    }
                }
            }
        }
        return docEl.getDocument();
    }

    private Map<String, String> getEnumConstantProperties(RootDoc doc, URLClassLoader loader, FieldDoc enumField) {
        Map<String, String> properties = new HashMap<String, String>();
        String classLoaderDescription = "URLClassLoader with these URLs: " + Arrays.asList(loader.getURLs()).toString();
        try {
            Class<?> loadedClass = loader.loadClass(enumField.containingClass().qualifiedName());

            try {
                Enum<?> enumConstant = Enum.valueOf((Class<Enum>) loadedClass, enumField.name());
                for (Method method : loadedClass.getDeclaredMethods()) {
                    if (!Modifier.isStatic(method.getModifiers()) && !method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 0) {
                        try {
                            String value = method.invoke(enumConstant).toString();
                            properties.put(method.getName(), value);
                        } catch (IllegalAccessException e) {
                            doc.printNotice("Could not get value from " + method.getName() + " because of this IllegalAccessException: " + e.getMessage());
                        } catch (IllegalArgumentException e) {
                            doc.printNotice("Could not get value from " + method.getName() + " because of this IllegalArgumentException: " + e.getMessage());
                        } catch (InvocationTargetException e) {
                            doc.printNotice("Could not get value from " + method.getName() + " because of this InvocationTargetException: " + e.getMessage());
                        }
                    }
                }
            } catch (NoClassDefFoundError e) {
                doc.printNotice("Could not load " + enumField.qualifiedName() + " using " + classLoaderDescription);
            }
        } catch (ClassNotFoundException e) {
            doc.printWarning("Class " + e.getMessage() + " not found using " + classLoaderDescription);
        }
        return properties;
    }
}
