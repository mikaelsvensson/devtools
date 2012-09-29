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
import info.mikaelsvensson.doctools.doclet.db2.metadata.Index;

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
