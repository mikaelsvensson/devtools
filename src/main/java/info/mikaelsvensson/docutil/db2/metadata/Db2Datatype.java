package info.mikaelsvensson.docutil.db2.metadata;

public enum Db2Datatype {
    /* String and text */
    VARCHAR(ColumnType.TEXT),
    CHARACTER(ColumnType.TEXT),

    /* Numeric */
    SMALLINT(ColumnType.NUMBER),
    INTEGER(ColumnType.NUMBER),
    INT(ColumnType.NUMBER),
    BIGINT(ColumnType.NUMBER),
    DECIMAL(ColumnType.NUMBER),
    NUMERIC(ColumnType.NUMBER),
    DECFLOAT(ColumnType.NUMBER),
    REAL(ColumnType.NUMBER),
    DOUBLE(ColumnType.NUMBER),

    /* Date and time */
    DATE(ColumnType.DATETIME),
    TIME(ColumnType.DATETIME),
    TIMESTAMP(ColumnType.DATETIME), ;

    private ColumnType type;

    private Db2Datatype(ColumnType type) {
        this.type = type;
    }

    public ColumnType getType() {
        return type;
    }

    public static Db2Datatype fromColumnDefinition(String sqlDefinition) {
        for (Db2Datatype datatype : values()) {
            if (sqlDefinition.toUpperCase().startsWith(datatype.name())) {
                return datatype;
            }
        }
        return null;
    }
}
