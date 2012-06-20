package se.linkon.sabine.docutil.xml.documentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import org.w3c.dom.Document;
import se.linkon.sabine.docutil.shared.DocumentWrapper;
import se.linkon.sabine.docutil.shared.ElementWrapper;

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
    /** @formatproperty */
    public static final String PARAMETER_CLASS_FOLDER = "classfolder";
    private File classFolder;

    public EnumDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException {
        super();
        classFolder = new File(parameters.get(PARAMETER_CLASS_FOLDER));
    }

    @Override
    public Document generateDocument(final RootDoc doc) {

        URLClassLoader loader = null;
        try {
            URL url = classFolder.toURI().toURL();
            loader = new URLClassLoader(new URL[]{url});
        } catch (MalformedURLException e) {
            doc.printWarning("Could not create URL class loader using (perhaps '" + classFolder.getAbsolutePath() + "' is not the correct the path to the class folder?). Detailed information about enum constants will not be available.");
        }

        DocumentWrapper docEl = new DocumentWrapper(createDocument("enumerations"));
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
                        Map<String, String> properties = getEnumConstantProperties(doc, loader, enumField);
                        for (Map.Entry<String, String> entry : properties.entrySet()) {
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
