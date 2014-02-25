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

public enum CommandType {
    CONNECT("CONNECT", new UnrelatedCommandHandler()),
    COMMIT("COMMIT", new UnrelatedCommandHandler()),
    TERMINATE("TERMINATE", new UnrelatedCommandHandler()),
    CREATE_SEQUENCE("CREATE SEQUENCE", new UnrelatedCommandHandler()),
    ALTER_SEQUENCE("ALTER SEQUENCE", new UnrelatedCommandHandler()),

    ALTER_TABLE("ALTER TABLE", new AlterTableCommandHandler()),
    CREATE_TABLE("CREATE TABLE", new CreateTableCommandHandler()),
    CREATE_INDEX("CREATE INDEX", new CreateIndexCommandHandler()),
    CREATE_INDEX_UNIQUE("CREATE UNIQUE INDEX", new CreateIndexCommandHandler());

    private String sql;
    private CommandHandler commandHandler;

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    private CommandType(String sql, CommandHandler commandHandler) {
        this.sql = sql;
        this.commandHandler = commandHandler;
    }

    public String getSql() {
        return sql;
    }

    public static CommandType fromSql(String sql) {
        for (CommandType type : values()) {
            if (sql.toLowerCase().startsWith(type.getSql().toLowerCase())) {
                return type;
            }
        }
        return null;
    }
}
