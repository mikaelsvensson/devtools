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


import info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2.metadata.Column;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2.metadata.Database;
import info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2.metadata.Table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTableCommandHandler extends AbstractCommandHandler {
    private static final Pattern COLUMNS_PATTERN = Pattern.compile("\\(\\s*([^,)]+)\\s");
    private static final Pattern COLUMN_DEFINITION_PATTERN = Pattern.compile("\\s*\"([a-zA-Z0-9_-]+)\"(.*)");


    @Override
    public void execute(Database db, String sql) {
        String name = getAffectedTableName(fixSQL(sql));

        Table table = new Table(name);

        table.addSqlCommand(sql);

        int posLeftParenthesis = fixSQL(sql).indexOf('(');
        int posRightParenthesis = fixSQL(sql).lastIndexOf(')');
        String columnsSql = fixSQL(sql).substring(posLeftParenthesis + 1, posRightParenthesis);
        for (String columnSql : columnsSql.split(",")) {
            Matcher matcher = COLUMN_DEFINITION_PATTERN.matcher(columnSql);
            if (matcher.matches()) {
                String columnName = matcher.group(1);
                String columnDefinition = matcher.group(2).trim();
                table.addColumn(new Column(columnName, columnDefinition));
            }
        }

        db.addTable(table);
    }
}
