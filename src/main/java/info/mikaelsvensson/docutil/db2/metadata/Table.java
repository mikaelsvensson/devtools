package info.mikaelsvensson.docutil.db2.metadata;

import java.util.ArrayList;
import java.util.List;

public class Table extends DatabaseObject{
    private List<Index> indexes = new ArrayList<Index>();
    private List<Column> columns = new ArrayList<Column>();

    public Table(String name) {
        super(name);
    }

    public void addIndex(Index index) {
        indexes.add(index);
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
