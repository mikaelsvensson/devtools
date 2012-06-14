package info.mikaelsvensson.doclet.xml;

import info.mikaelsvensson.doclet.shared.DocumentCreator;
import info.mikaelsvensson.doclet.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.doclet.xml.documentcreator.StandardDocumentCreator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocletOptions {
    public static final String PARAMETER_FORMAT = "-format";
    public static final String PARAMETER_OUTPUT = "-output";
    public static final String PARAMETER_TRANSFORMER = "-transformer";

    private Map<String, XmlDocletAction> actions = new HashMap<String, XmlDocletAction>();

    public Map<String, XmlDocletAction> getActions() {
        return actions;
    }
    public XmlDocletAction getAction(String key) {
        if (!actions.containsKey(key)) {
            actions.put(key, new XmlDocletAction());
        }
        return actions.get(key);
    }

    public static int optionLength(String option) {
        if (option.startsWith(PARAMETER_FORMAT) || option.startsWith(PARAMETER_OUTPUT) || option.startsWith(PARAMETER_TRANSFORMER)) {
            return 2;
        }
        return 0;
    }

    public static XmlDocletOptions load(String[][] args) {
        XmlDocletOptions options = new XmlDocletOptions();
        for (String[] arg : args) {
            String argName = arg[0];
            if (argName.startsWith(PARAMETER_FORMAT)) {
                String key = argName.substring(PARAMETER_FORMAT.length());
                XmlDocletAction.Format format = XmlDocletAction.Format.valueOfSimple(arg[1]);
                if (format != null) {
                    options.getAction(key).setFormat(format);
                } else {
                    throw new IllegalArgumentException(arg[1] + " is not a recognized format.");
                }
            } else if (argName.startsWith(PARAMETER_OUTPUT)) {
                String key = argName.substring(PARAMETER_OUTPUT.length());
                options.getAction(key).setOutput(new File(arg[1]));
            } else if (argName.startsWith(PARAMETER_TRANSFORMER)) {
                String key = argName.substring(PARAMETER_TRANSFORMER.length());
                options.getAction(key).setTransformer(new File(arg[1]));
            }
        }
        return options;
    }
}
