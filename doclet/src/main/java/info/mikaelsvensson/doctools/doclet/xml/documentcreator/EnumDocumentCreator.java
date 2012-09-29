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

package info.mikaelsvensson.doctools.doclet.xml.documentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.doctools.doclet.shared.DocumentCreatorException;
import info.mikaelsvensson.doctools.doclet.shared.DocumentWrapper;
import info.mikaelsvensson.doctools.doclet.shared.ElementWrapper;
import info.mikaelsvensson.doctools.doclet.shared.propertyset.PropertySet;
import info.mikaelsvensson.doctools.doclet.xml.FormatProperty;
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

public class EnumDocumentCreator extends AbstractDocumentCreator {
    public static final String NAME = "enum";

    /**
     * Specified where the compiled classes (*.class files) can be found. The class file folder is used to instantiate
     * enum classes in order to invoke their getters and retrieve their return values.
     */
    @FormatProperty
    public static final String PARAMETER_CLASS_FOLDER = "classfolder";
//    private File classFolder;

/*
    public EnumDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException {
        super();
        classFolder = new File(parameters.get(PARAMETER_CLASS_FOLDER));
    }
*/

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

        DocumentWrapper docEl = null;
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
