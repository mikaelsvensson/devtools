package info.mikaelsvensson.docutil.shared;

import info.mikaelsvensson.docutil.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.docutil.xml.documentcreator.EnumDocumentCreator;
import info.mikaelsvensson.docutil.xml.documentcreator.ExtensiveDocumentCreator;
import info.mikaelsvensson.docutil.xml.documentcreator.StandardDocumentCreator;

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
