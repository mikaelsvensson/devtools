package info.mikaelsvensson.doclet.xml;

import info.mikaelsvensson.doclet.shared.DocumentCreator;

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

    public XmlDocletOptions(String[][] args) {
        init(args);
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

    private void init(String[][] args) {
        actions = new HashMap<String, XmlDocletAction>();
        for (String[] arg : args) {
            initOption(arg);
        }
    }

    protected void initOption(String[] arg) {
        String argName = arg[0];
        if (argName.startsWith(PARAMETER_FORMAT)) {
            String key = argName.substring(PARAMETER_FORMAT.length());
//            XmlDocletAction.Format format = XmlDocletAction.Format.valueOfSimple(arg[1]);
            DocumentCreator documentCreator = createDocumentCreator(arg[1]);
            if (documentCreator != null) {
                getAction(key).setDocumentCreator(documentCreator);
            } else {
                throw new IllegalArgumentException(arg[1] + " is not a recognized format.");
            }
        } else if (argName.startsWith(PARAMETER_OUTPUT)) {
            String key = argName.substring(PARAMETER_OUTPUT.length());
            getAction(key).setOutput(new File(arg[1]));
        } else if (argName.startsWith(PARAMETER_TRANSFORMER)) {
            String key = argName.substring(PARAMETER_TRANSFORMER.length());
            getAction(key).setTransformer(new File(arg[1]));
        }
    }

/*
    public static XmlDocletOptions load(String[][] args) {
        return new XmlDocletOptions(args);
    }
*/

    protected DocumentCreator createDocumentCreator(String name) {
        XmlDocletAction.Format format = XmlDocletAction.Format.valueOfSimple(name);
        if (null != format) {
            try {
                return format.createDocumentCreator();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }
}
