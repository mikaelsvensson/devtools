package info.mikaelsvensson.doclet.db2.parser;

public abstract class AbstractCommandHandler implements CommandHandler {
    protected String getAffectedTableName(String sql) {
        int posDot = sql.indexOf('.');
        int posSpace = sql.indexOf(' ', posDot);
        return sql.substring(posDot + 1 + 1, posSpace - 1);
    }
}
