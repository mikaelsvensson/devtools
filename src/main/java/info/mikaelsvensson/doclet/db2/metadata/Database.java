package info.mikaelsvensson.doclet.db2.metadata;

import java.util.ArrayList;
import java.util.List;

public class Database extends DatabaseObject {
    private List<Table> tables = new ArrayList<Table>();

    public List<Table> getTables() {
        return tables;
    }

    public boolean addTable(Table table) {
        return tables.add(table);
    }

    public Database(String name) {
        super(name);
    }

    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }
}
