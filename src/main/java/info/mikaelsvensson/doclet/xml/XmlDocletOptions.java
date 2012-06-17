package info.mikaelsvensson.doclet.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class XmlDocletOptions<T extends XmlDocletAction> {
    public static final String PARAMETER_FORMAT = "-format";
    public static final String PARAMETER_OUTPUT = "-output";
    public static final String PARAMETER_TRANSFORMER = "-transformer";
    public static final String PARAMETER_PROPERTIES_FILE = "-properties";
    public static final String PARAMETER_PROPERTY = "-property";

    private Map<String, T> actions = new HashMap<String, T>();

    public Map<String, T> getActions() {
        return actions;
    }

    public XmlDocletOptions(String[][] args) {
        init(args);
    }

    public T getAction(String key) {
        if (!actions.containsKey(key)) {
            actions.put(key, createAction());
        }
        return actions.get(key);
    }

    protected T createAction() {
        return (T) new XmlDocletAction();
    }

    public static int optionLength(String option) {
        if (option.startsWith(PARAMETER_FORMAT) || option.startsWith(PARAMETER_OUTPUT) || option.startsWith(PARAMETER_TRANSFORMER) || option.startsWith(PARAMETER_PROPERTIES_FILE) || option.startsWith(PARAMETER_PROPERTY)) {
            return 2;
        }
        return 0;
    }

    private void init(String[][] args) {
        actions = new HashMap<String, T>();
        for (String[] arg : args) {
            initOption(arg);
        }
    }

    protected void initOption(String[] arg) {
        String argName = arg[0];
        if (argName.startsWith(PARAMETER_FORMAT)) {
            String key = argName.substring(PARAMETER_FORMAT.length());
            getAction(key).setFormat(arg[1]);

/*
            XmlDocletAction.Format format = XmlDocletAction.Format.valueOfSimple(arg[1]);
//            DocumentCreator documentCreator = createDocumentCreator(arg[1]);
            if (format != null) {
                getAction(key).setFormat(format);
//                getAction(key).setDocumentCreator(documentCreator);
            } else {
                throw new IllegalArgumentException(arg[1] + " is not a recognized format.");
            }
*/
        } else if (argName.startsWith(PARAMETER_OUTPUT)) {
            String key = argName.substring(PARAMETER_OUTPUT.length());
            getAction(key).setOutput(new File(arg[1]));
        } else if (argName.startsWith(PARAMETER_TRANSFORMER)) {
            String key = argName.substring(PARAMETER_TRANSFORMER.length());
            getAction(key).setTransformer(new File(arg[1]));
        } else if (argName.startsWith(PARAMETER_PROPERTY)) {
            String key = argName.substring(PARAMETER_PROPERTY.length());
            int pos = arg[1].indexOf('=');
            getAction(key).getParameters().put(arg[1].substring(0, pos), arg[1].substring(pos + 1));
        } else if (argName.startsWith(PARAMETER_PROPERTIES_FILE)) {
            String key = argName.substring(PARAMETER_PROPERTIES_FILE.length());
            Properties properties = new Properties();
            try {
                properties.load(new FileReader(arg[1]));
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    getAction(key).getParameters().put(entry.getKey().toString(), entry.getValue().toString());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(arg[1] + " could not be loaded. " + e.getMessage());
            }
        }
    }

/*
    protected DocumentCreator createDocumentCreator(String name) {
        XmlDocletAction.Format format = XmlDocletAction.Format.valueOfSimple(name);
        if (null != format) {
            try {
                return format.createDocumentCreator(action.getParameters());
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }
*/
}
