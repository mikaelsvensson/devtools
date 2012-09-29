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

package info.mikaelsvensson.docutil.db2.parser;

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
