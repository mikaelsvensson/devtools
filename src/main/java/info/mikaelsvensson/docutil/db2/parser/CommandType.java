package info.mikaelsvensson.docutil.db2.parser;

public enum CommandType {
    CONNECT("CONNECT", new UnrelatedCommandHandler()),
    COMMIT("COMMIT", new UnrelatedCommandHandler()),
    TERMINATE("TERMINATE", new UnrelatedCommandHandler()),
    CREATE_SEQUENCE("CREATE SEQUENCE", new UnrelatedCommandHandler()),
    ALTER_SEQUENCE("ALTER SEQUENCE", new UnrelatedCommandHandler()),

    ALTER_TABLE("ALTER TABLE", new AlterTableCommandHandler()),
    CREATE_TABLE("CREATE TABLE", new CreateTableCommandHandler()),
    CREATE_INDEX("CREATE INDEX", new CreateIndexCommandHandler()),
    CREATE_INDEX_UNIQUE("CREATE UNIQUE INDEX", new CreateIndexCommandHandler());

    private String sql;
    private CommandHandler commandHandler;

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    private CommandType(String sql, CommandHandler commandHandler) {
        this.sql = sql;
        this.commandHandler = commandHandler;
    }

    public String getSql() {
        return sql;
    }

    public static CommandType fromSql(String sql) {
        for (CommandType type : values()) {
            if (sql.toLowerCase().startsWith(type.getSql().toLowerCase())) {
                return type;
            }
        }
        return null;
    }
}
