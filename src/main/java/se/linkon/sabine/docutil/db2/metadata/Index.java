package se.linkon.sabine.docutil.db2.metadata;

import java.util.ArrayList;
import java.util.List;

public class Index extends DatabaseObject {

    private List<String> columns = new ArrayList<String>();

    private boolean unique;

    public Index(String name, boolean unique, List<String> columns) {
        super(name);
        setColumns(columns);
        this.unique = unique;
    }

    public List<String> getColumns() {
        return columns;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void addColumn(String column) {
        this.columns.add(column);
    }
}
