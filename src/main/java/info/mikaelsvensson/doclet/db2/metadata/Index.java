package info.mikaelsvensson.doclet.db2.metadata;

import java.util.ArrayList;
import java.util.List;

public class Index extends DatabaseObject{
    
    private List<Column> columns = new ArrayList<Column>();

    public List<Column> getColumns() {
        return columns;
    }

    public Index(String name, List<Column> columns) {
        super(name);
        this.columns = columns;
    }

    public Index(String name) {
        super(name);
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }
}
