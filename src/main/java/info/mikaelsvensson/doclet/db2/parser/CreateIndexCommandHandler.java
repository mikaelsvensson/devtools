package info.mikaelsvensson.doclet.db2.parser;


import info.mikaelsvensson.doclet.db2.metadata.Database;

public class CreateIndexCommandHandler extends AbstractCommandHandler {
    @Override
    public void execute(Database db, String sql) {
        System.out.println("CREATE INDEX: " + sql);
//        db.addTable(new Table(getAffectedTableName(sql)));
    }
}
