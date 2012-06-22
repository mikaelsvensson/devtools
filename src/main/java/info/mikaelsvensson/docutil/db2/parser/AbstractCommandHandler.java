package info.mikaelsvensson.docutil.db2.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCommandHandler implements CommandHandler {
    protected static final String REGEXP_QUOTED_NAME = "\"([a-zA-Z0-9_-]+)\"";
    protected static final String REGEXP_QUOTED_NAMES_IN_PARENTHESIS = "\\(((" + REGEXP_QUOTED_NAME + "[\\s,ASCDEascde]*)+)\\)";
    protected static final Pattern QUOTED_NAME_PATTERN = Pattern.compile(REGEXP_QUOTED_NAME);

    public static String getAffectedTableName(String sql) {
        int posDot = sql.indexOf('.');
        int posSpace = sql.indexOf(' ', posDot);
        return sql.substring(posDot + 1 + 1, posSpace - 1);
    }

    protected List<String> getColumns(String columnListSql) {
        List<String> cols = new LinkedList<String>();
        Matcher colMatcher = QUOTED_NAME_PATTERN.matcher(columnListSql);
        while (colMatcher.find()) {
            cols.add(colMatcher.group(1));
        }
        return cols;
    }

    protected String fixSQL(String sql) {
        return sql.replaceAll("\n", "");
    }
}
