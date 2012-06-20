package se.linkon.sabine.docutil.xml;


import se.linkon.sabine.docutil.shared.propertyset.PropertySet;
import se.linkon.sabine.docutil.shared.propertyset.PropertySetException;

import java.util.HashMap;
import java.util.Map;

public class XmlDocletOptions<T extends XmlDocletAction> {
    public static final String ACTION = "action";
    public static final String PROPERTIES_FILE = "propertiesfile";

    private Map<String, T> actions = new HashMap<String, T>();

    protected PropertySet propertySet;

    public Map<String, T> getActions() {
        return actions;
    }

    public XmlDocletOptions(PropertySet propertySet) throws PropertySetException {
        System.out.println(propertySet);
        String propertiesfile = propertySet.getProperty(PROPERTIES_FILE);
        if (null != propertiesfile) {
            propertySet.loadFromFile(propertiesfile);

        }
        for (Map.Entry<String, PropertySet> entry : propertySet.getCollection(ACTION).entrySet()) {
            actions.put(entry.getKey(), createAction(entry.getValue()));
        }
    }

    protected T createAction(PropertySet propertySet) {
        return (T) new XmlDocletAction(propertySet);
    }

    public static int optionLength(String option) {
        if (option.startsWith("-" + ACTION)) {
            return 2;
        }
        return 0;
    }

}
