package info.mikaelsvensson.docutil.db2.parser;


import info.mikaelsvensson.docutil.db2.metadata.Database;
import info.mikaelsvensson.docutil.db2.metadata.Index;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateIndexCommandHandler extends AbstractCommandHandler {

    /*
    Create unique index:
    CREATE UNIQUE INDEX "SPACELOADTEST"."IDX1205310837580" ON "SPACELOADTEST"."TRANSPORTDEPARTURE_DEPARTUREBLOCKING"
		("TRANSPORTDEPARTURE_PK" ASC,
		 "BLOCKINGS_PK" DESC)
    CREATE UNIQUE INDEX "SPACELOADTEST"."IDX1205310850490" ON "SPACELOADTEST"."PRODUCER"
		("PK" ASC)
		INCLUDE ("PRODUCERID" ,
		 "NAME" ,
		 "ACTIVE" ,
		 "VERSION" )

     - look for UNIQUE INDEX schema.name ON schema.name ( [name ASC|DESC]* )
     */
    private static final Pattern CREATE_INDEX_PATTERN = Pattern.compile(".*" +
            "(UNIQUE INDEX)\\s*" + REGEXP_QUOTED_NAME + "." + REGEXP_QUOTED_NAME + "\\s*" +
            "ON\\s*\\" + REGEXP_QUOTED_NAME + "." + REGEXP_QUOTED_NAME + "\\s*" + REGEXP_QUOTED_NAMES_IN_PARENTHESIS + "\\s*" +
            "INCLUDE\\s*"+REGEXP_QUOTED_NAMES_IN_PARENTHESIS+".*");

    @Override
    public void execute(Database db, String sql) {
        Matcher addUniqueMatcher = CREATE_INDEX_PATTERN.matcher(fixSQL(sql));
        if (addUniqueMatcher.matches()) {
            boolean isUnique = addUniqueMatcher.group(1).toUpperCase().contains("UNIQUE");
            String table = addUniqueMatcher.group(5);
            String name = addUniqueMatcher.group(3);

            db.getTable(table).addSqlCommand(sql);

            List<String> columns = getColumns(addUniqueMatcher.group(6));
            List<String> inludeColumns = getColumns(addUniqueMatcher.group(9));
            columns.addAll(inludeColumns);

            db.getTable(table).addIndex(new Index(name, isUnique, columns));
        } else {
//            System.out.println("Unknown CREATE INDEX command: " + sql);
        }
    }
}
