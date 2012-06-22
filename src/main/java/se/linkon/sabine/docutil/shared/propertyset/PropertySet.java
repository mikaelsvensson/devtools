package se.linkon.sabine.docutil.shared.propertyset;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertySet {
    public static final char SEPARATOR = '.';
    private Map<String, PropertySet> collections = new HashMap<String, PropertySet>();
    private Map<String, String> properties = new HashMap<String, String>();

    public PropertySet(String[][] options) throws PropertySetException {
        for (String[] option : options) {
            if (option.length == 2) {
                setProperty(option[0].substring(1), option[1]);
            }
        }
    }

    public PropertySet() {
    }

    /**
     *
     * @param key
     * @param value
     * @throws PropertySetException if the {@code key} expression, in one way or another, is invalid/incorrect.
     */
    public void setProperty(String key, String value) throws PropertySetException {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            if (collections.containsKey(key)) {
                throw new PropertySetException(key + " cannot be set to a single value since it is already a property set, " + collections.get(key));
            }
            properties.put(key, value);
        } else {
            String childKey = key.substring(0, pos);
            if (!collections.containsKey(childKey)) {
                if (properties.containsKey(childKey)) {
                    throw new PropertySetException(childKey + " cannot be the name of a collection since it is already a single value, " + properties.get(childKey));
                }
                collections.put(childKey, new PropertySet());
            }
            PropertySet childSet = collections.get(childKey);
            if (null == childSet) {
                throw new PropertySetException("Could not find collection " + childKey + " in this list of collections: " + collections.keySet().toString());
            }
            childSet.setProperty(key.substring(pos + 1), value);
        }
    }

    /**
     *
     * @param key
     * @return a single property value, or {@code null} if {@code key} does not correctly specify a property name.
     */
    public String getProperty(String key) {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            return properties.get(key);
        } else {
            PropertySet childSet = collections.get(key.substring(0, pos));
            if (childSet != null) {
                return childSet.getProperty(key.substring(pos + 1));
            } else {
                return null;
            }
        }
    }
    public PropertySet getPropertySet(String key) {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            if (collections.containsKey(key)) {
                return collections.get(key);
            } else {
                return new PropertySet();
            }
        } else {
            PropertySet childSet = collections.get(key.substring(0, pos));
            if (childSet != null) {
                return childSet.getPropertySet(key.substring(pos + 1));
            } else {
                return null;
            }
        }
    }

    /**
     *
     * @param key
     * @return map with named property sets. Returns {@code null} if the {@code key} is invalid.
     */
    public Map<String, PropertySet> getCollection(String key) {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            PropertySet propertySet = collections.get(key);
            if (null == propertySet) {
                //TODO: MISV 20120619 remove STDOUT debugging and document return value in javadoc
                System.out.println(this.toString() + " does not contain a property set called " + key);
                return new HashMap<String, PropertySet>();
            }
            return propertySet.collections;
        } else {
            PropertySet childSet = collections.get(key.substring(0, pos));
            if (null == childSet) {
                //TODO: MISV 20120619 reason for return value in javadoc
                return new HashMap<String, PropertySet>();
            }
            return childSet.getCollection(key.substring(pos + 1));
        }
    }

    @Override
    public String toString() {
        return "{" +
                "properties=" + properties + ", " +
                "collections=\n" + collections +
                "}\n";
    }

    /**
     *
     * @param key
     * @return map with properties. An empty (non-null) map will be returned if the {@code key} is invalid.
     */
    public Map<String, String> getProperties(String key) {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            PropertySet propertySet = collections.get(key);
            if (null == propertySet) {
                //TODO: MISV 20120619 remove STDOUT debugging and document return value in javadoc
                System.out.println(this.toString() + " does not contain a property set called " + key);
                return new HashMap<String, String>();
            }
            return propertySet.properties;
        } else {
            String collectionName = key.substring(0, pos);
            PropertySet childSet = collections.get(collectionName);
            if (null == childSet) {
                //TODO: MISV 20120619 remove STDOUT debugging and document return value in javadoc
                System.out.println("Could not find collection " + collectionName + " amongst " + collections.keySet().toString());
                return new HashMap<String, String>();
            }
            return childSet.getProperties(key.substring(pos + 1));
        }
    }

    /**
     *
     * @param propertiesFilePath path to file with properties
     * @throws se.linkon.sabine.docutil.shared.propertyset.PropertySetException thrown if file cannot be read of if one of its property assignments is invalid in some way.
     */
    public void loadFromFile(String propertiesFilePath) throws PropertySetException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertiesFilePath));
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                setProperty(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            //TODO: MISV 20120619 remove STDOUT debugging and document thrown exception javadoc
            throw new PropertySetException(propertiesFilePath + " could not be loaded. " + e.getMessage());
        }
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
