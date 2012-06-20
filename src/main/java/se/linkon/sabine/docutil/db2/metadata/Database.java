package se.linkon.sabine.docutil.db2.metadata;

import java.util.*;

public class Database extends DatabaseObject {
    private List<Table> tables = new ArrayList<Table>();
    private Map<String, String> properties = new TreeMap<String, String>();
    public Date getTimeStamp() {
        return timeStamp;
    }

    private Date timeStamp;

    public List<Table> getTables() {
        return tables;
    }

    public boolean addTable(Table table) {
        return tables.add(table);
    }

    public Database() {
        super("Untitled");
    }

    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public void setTimeStamp(Date date) {
        this.timeStamp = date;
    }
}
