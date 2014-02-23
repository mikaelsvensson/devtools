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

public class ForeignKey extends DatabaseObject {

    private String column;
    private String referencedSchema;
    private String referencedTable;
    private String referencedCol;

    public ForeignKey(String name, String col, String refSchema, String refTable, String refCol) {
        super(name);
        this.column = col;
        this.referencedSchema = refSchema;
        this.referencedTable = refTable;
        this.referencedCol = refCol;
    }

    public String getColumn() {
        return column;
    }

    public String getReferencedSchema() {
        return referencedSchema;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public String getReferencedCol() {
        return referencedCol;
    }
}
