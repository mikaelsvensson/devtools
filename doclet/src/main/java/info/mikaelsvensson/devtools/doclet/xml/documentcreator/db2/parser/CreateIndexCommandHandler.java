/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2.parser;


import info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2.metadata.Database;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2.metadata.Index;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateIndexCommandHandler extends AbstractCommandHandler {

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
