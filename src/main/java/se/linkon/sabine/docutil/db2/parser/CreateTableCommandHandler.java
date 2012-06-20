package se.linkon.sabine.docutil.db2.parser;


import se.linkon.sabine.docutil.db2.metadata.Column;
import se.linkon.sabine.docutil.db2.metadata.Database;
import se.linkon.sabine.docutil.db2.metadata.Table;

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
