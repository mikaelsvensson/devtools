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

package info.mikaelsvensson.docutil.db2.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table extends DatabaseObject {
    private List<Index> indexes = new ArrayList<Index>();
    private List<Column> columns = new ArrayList<Column>();
    private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
    private String[] primaryKey = null;
    private List<String> sqlCommands = new ArrayList<String>();

    public List<String> getSqlCommands() {
        return Collections.unmodifiableList(sqlCommands);
    }

    public boolean addSqlCommand(String sql) {
        return sqlCommands.add(sql);
    }


    public String[] getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String[] primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Table(String name) {
        super(name);
    }

    public void addIndex(Index index) {
        indexes.add(index);
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void addForeignKey(ForeignKey foreignKey) {
        foreignKeys.add(foreignKey);
    }

    public List<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public boolean isReferenceColumn(Column column) {
        for (ForeignKey foreignKey : foreignKeys) {
            if (foreignKey.getColumn().equals(column.getName())) {
                return true;
            }
        }
        return false;
    }
}
