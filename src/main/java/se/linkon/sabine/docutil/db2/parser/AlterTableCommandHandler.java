package se.linkon.sabine.docutil.db2.parser;


import se.linkon.sabine.docutil.db2.metadata.Database;
import se.linkon.sabine.docutil.db2.metadata.ForeignKey;
import se.linkon.sabine.docutil.db2.metadata.Index;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlterTableCommandHandler extends AbstractCommandHandler {

    private static final Pattern ADD_CONSTRAINT_PATTERN = Pattern.compile(".*ADD CONSTRAINT\\s*" + REGEXP_QUOTED_NAME + "\\s*FOREIGN KEY\\s*\\(" + REGEXP_QUOTED_NAME + "\\)\\s*REFERENCES \\s*" + REGEXP_QUOTED_NAME + "\\." + REGEXP_QUOTED_NAME + "\\s*\\(" + REGEXP_QUOTED_NAME + "\\).*");
    private static final Pattern ADD_PRIMARY_KEY_PATTERN = Pattern.compile(".*ADD PRIMARY KEY\\s*" + REGEXP_QUOTED_NAMES_IN_PARENTHESIS + ".*");
    private static final Pattern ADD_UNIQUE_PATTERN = Pattern.compile(".*ADD UNIQUE\\s*" + REGEXP_QUOTED_NAMES_IN_PARENTHESIS + ".*");


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
        String table = getAffectedTableName(fixSQL(sql));

        db.getTable(table).addSqlCommand(sql);

        Matcher addConstraintMatcher = ADD_CONSTRAINT_PATTERN.matcher(fixSQL(sql));
        if (addConstraintMatcher.matches()) {
            String name = addConstraintMatcher.group(1);
            String col = addConstraintMatcher.group(2);
            String refSchema = addConstraintMatcher.group(3);
            String refTable = addConstraintMatcher.group(4);
            String refCol = addConstraintMatcher.group(5);
            db.getTable(table).addForeignKey(new ForeignKey(name, col, refSchema, refTable, refCol));
        } else {
            Matcher addPrimaryKeyMatcher = ADD_PRIMARY_KEY_PATTERN.matcher(fixSQL(sql));
            if (addPrimaryKeyMatcher.matches()) {
                List<String> columns = getColumns(addPrimaryKeyMatcher.group(1));
                db.getTable(table).setPrimaryKey(columns.toArray(new String[] {}));
            } else {
                Matcher addUniqueMatcher = ADD_UNIQUE_PATTERN.matcher(fixSQL(sql));
                if (addUniqueMatcher.matches()) {
                    List<String> columns = getColumns(addUniqueMatcher.group(1));
                    db.getTable(table).addIndex(new Index("", true, columns));
                } else {
//                    System.out.println("Unknown ALTER TABLE command: " + sql);
                }
            }
        }
    }

}
