package info.mikaelsvensson.docutil.db2.metadata;

public class ForeignKey extends DatabaseObject {

    private String column;
    private String referencedSchema;
    private String referencedTable;
    private String referencedCol;

    public ForeignKey(String name, String col, String refSchema, String refTable, String refCol) {
        super(name);
        this.column = col;
        this.referencedSchema = refSchema;
        this.referencedTable = refTable;
        this.referencedCol = refCol;
    }

    public String getColumn() {
        return column;
    }

    public String getReferencedSchema() {
        return referencedSchema;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public String getReferencedCol() {
        return referencedCol;
    }
}
