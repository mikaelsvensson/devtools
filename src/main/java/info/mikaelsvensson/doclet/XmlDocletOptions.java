package info.mikaelsvensson.doclet;

import info.mikaelsvensson.doclet.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.doclet.documentcreator.StandardDocumentCreator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class XmlDocletOptions {
    public static final String PARAMETER_FORMAT = "-format";
    public static final String PARAMETER_OUTPUT = "-output";
    public static final String PARAMETER_TRANSFORMER = "-transformer";
    public static final String PARAMETER_PROPERTIES_FILE = "-properties";
    private DocumentCreator documentCreator;

    private Map<String, XmlDocletAction> actions = new HashMap<String, XmlDocletAction>();

    public DocumentCreator getDocumentCreator() {
        return documentCreator;
    }

    public Map<String, XmlDocletAction> getActions() {
        return actions;
    }

    public static int optionLength(String option) {
        if (option.equals(PARAMETER_FORMAT) || option.startsWith(PARAMETER_OUTPUT) || option.startsWith(PARAMETER_TRANSFORMER) || option.startsWith(PARAMETER_PROPERTIES_FILE)) {
            return 2;
        }
        return 0;
    }

    public static XmlDocletOptions load(String[][] args) {
        XmlDocletOptions options = new XmlDocletOptions();
        for (String[] arg : args) {
            String argName = arg[0];
//            String[] values = Arrays.copyOfRange(arg, 1, arg.length);
            if (PARAMETER_FORMAT.equals(argName)) {
                try {
                    if (StandardDocumentCreator.class.getSimpleName().toLowerCase().equals(arg[1].toLowerCase())) {
                        options.documentCreator = new StandardDocumentCreator();
                    } else if (ElementsOnlyDocumentCreator.class.getSimpleName().toLowerCase().equals(arg[1].toLowerCase())) {
                        options.documentCreator = new ElementsOnlyDocumentCreator();
                    } else {
                        throw new IllegalArgumentException(arg[1] + " is not a recognized format.");
                    }
                } catch (ParserConfigurationException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            } else if (argName.startsWith(PARAMETER_OUTPUT)) {
                String key = argName.substring(PARAMETER_OUTPUT.length());
                if (!options.getActions().containsKey(key)) {
                    options.getActions().put(key, new XmlDocletAction());
                }
                options.getActions().get(key).setOutput(new File(arg[1]));
            } else if (argName.startsWith(PARAMETER_TRANSFORMER)) {
                String key = argName.substring(PARAMETER_TRANSFORMER.length());
                if (!options.getActions().containsKey(key)) {
                    options.getActions().put(key, new XmlDocletAction());
                }
                options.getActions().get(key).setTransformer(new File(arg[1]));
            } else if (argName.startsWith(PARAMETER_PROPERTIES_FILE)) {
                String key = argName.substring(PARAMETER_PROPERTIES_FILE.length());
                if (!options.getActions().containsKey(key)) {
                    options.getActions().put(key, new XmlDocletAction());
                }
                Properties properties = new Properties();
                try {
                    properties.load(new FileReader(arg[1]));
                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                        options.getActions().get(key).getParameters().put(entry.getKey().toString(), entry.getValue().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return options;
    }
}
