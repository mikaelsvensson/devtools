package info.mikaelsvensson.doclet;

import info.mikaelsvensson.doclet.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.doclet.documentcreator.StandardDocumentCreator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocletOptions {
    public static final String PARAMETER_FORMAT = "-format";
    public static final String PARAMETER_OUTPUT = "-output";
    public static final String PARAMETER_TRANSFORMER = "-transformer";
    private DocumentCreator documentCreator;

    private Map<String, XmlDocletAction> actions = new HashMap<String, XmlDocletAction>();

    public DocumentCreator getDocumentCreator() {
        return documentCreator;
    }

    public Map<String, XmlDocletAction> getActions() {
        return actions;
    }

    public static int optionLength(String option) {
        if (option.equals(PARAMETER_FORMAT) || option.startsWith(PARAMETER_OUTPUT) || option.startsWith(PARAMETER_TRANSFORMER)) {
            return 2;
        }
        return 0;
    }

    public static XmlDocletOptions load(String[][] args) {
        XmlDocletOptions options = new XmlDocletOptions();
        for (String[] arg : args) {
            String argName = arg[0];
//            String[] values = Arrays.copyOfRange(arg, 1, arg.length);
            String argValue = arg[1];
            if (PARAMETER_FORMAT.equals(argName)) {
                try {
                    if (StandardDocumentCreator.class.getSimpleName().toLowerCase().equals(argValue.toLowerCase())) {
                        options.documentCreator = new StandardDocumentCreator();
                    } else if (ElementsOnlyDocumentCreator.class.getSimpleName().toLowerCase().equals(argValue.toLowerCase())) {
                        options.documentCreator = new ElementsOnlyDocumentCreator();
                    } else {
                        throw new IllegalArgumentException(argValue + " is not a recognized format.");
                    }
                } catch (ParserConfigurationException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            } else if (argName.startsWith(PARAMETER_OUTPUT)) {
                String key = argName.substring(PARAMETER_OUTPUT.length());
                if (!options.getActions().containsKey(key)) {
                    options.getActions().put(key, new XmlDocletAction());
                }
                options.getActions().get(key).setOutput(new File(argValue));
            } else if (argName.startsWith(PARAMETER_TRANSFORMER)) {
                String key = argName.substring(PARAMETER_TRANSFORMER.length());
                if (!options.getActions().containsKey(key)) {
                    options.getActions().put(key, new XmlDocletAction());
                }
                options.getActions().get(key).setOutput(new File(argValue));
            }
        }
        return options;
    }
}
