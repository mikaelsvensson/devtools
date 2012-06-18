package info.mikaelsvensson.doclet.shared.propertyset;

import java.util.HashMap;
import java.util.Map;

public class PropertySet {
    private Map<String, PropertySet> childSets = new HashMap<String, PropertySet>();
    private Map<String, String> properties = new HashMap<String, String>();

    public void setProperty(String key, String value) throws PropertySetException {
        int pos = key.indexOf('.');
        if (pos == -1) {
            if (childSets.containsKey(key)) {
                throw new PropertySetException(key + " cannot be set to a single value since it is already a property set, " + childSets.get(key));
            }
            properties.put(key, value);
        } else {
            String childKey = key.substring(0, pos);
            if (!childSets.containsKey(childKey)) {
                if (properties.containsKey(childKey)) {
                    throw new PropertySetException(childKey + " cannot be the name of a collection since it is already a single value, " + properties.get(childKey));
                }
                childSets.put(childKey, new PropertySet());
            }
            PropertySet childSet = childSets.get(childKey);

            childSet.setProperty(key.substring(pos + 1), value);
        }
    }

    public String getProperty(String key) {
        int pos = key.indexOf('.');
        if (pos == -1) {
            return properties.get(key);
        } else {
            PropertySet childSet = childSets.get(key.substring(0, pos));
            if (childSet != null) {
                return childSet.getProperty(key.substring(pos + 1));
            } else {
                return null;
            }
        }
    }

    public Map<String, PropertySet> getCollection(String key) {
        int pos = key.indexOf('.');
        if (pos == -1) {
            return childSets.get(key).childSets;
        } else {
            PropertySet childSet = childSets.get(key.substring(0, pos));

            return childSet.getCollection(key.substring(pos + 1));
        }
    }

    @Override
    public String toString() {
        return "{" +
                "properties=" + properties + ", " +
                "collections=" + childSets +
                '}';
    }
}
