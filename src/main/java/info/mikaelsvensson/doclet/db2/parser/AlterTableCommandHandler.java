package info.mikaelsvensson.doclet.db2.parser;


import info.mikaelsvensson.doclet.db2.metadata.Database;

public class AlterTableCommandHandler extends AbstractCommandHandler {

    /*
    Add constraint:
    ADD CONSTRAINT "FK42E810292EC5ED3" FOREIGN KEY ("DEPARTUREREPORTMAILTIME_PK") REFERENCES "SPACELOADTEST"."DEPARTUREREPORTMAILTIME" ("PK")
     -  look for "FOREIGN KEY", a column name, "REFERENCES", a table name and a column name

    Add primary key:
	ADD PRIMARY KEY ("PK");
	 - Look for single \"[a-zA-Z_]\"


    Add unique index:
	ADD UNIQUE ("USER_PK", "PRODUCER_PK", "ROLE");
	 - Look for multiple \"[a-zA-Z_]\"

    */

    @Override
    public void execute(Database db, String sql) {
        System.out.println("ALTER TABLE: " + sql);
//        db.addTable(new Table(getAffectedTableName(sql)));
    }

}
