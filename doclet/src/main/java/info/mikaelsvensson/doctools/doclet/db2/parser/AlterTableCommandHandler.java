/*
 * Copyright (c) 2012, Mikael Svensson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the copyright holder nor the names of the
 *       contributors of this software may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package info.mikaelsvensson.doctools.doclet.db2.parser;

import info.mikaelsvensson.doctools.doclet.db2.metadata.Database;
import info.mikaelsvensson.doctools.doclet.db2.metadata.ForeignKey;
import info.mikaelsvensson.doctools.doclet.db2.metadata.Index;

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
