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

package info.mikaelsvensson.devtools.doclet.db2.metadata;

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
