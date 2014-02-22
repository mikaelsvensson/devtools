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

package info.mikaelsvensson.devtools.doclet.db2.parser;

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
