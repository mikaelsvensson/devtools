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

public enum Db2Datatype {
    /* String and text */
    VARCHAR(ColumnType.TEXT),
    CHARACTER(ColumnType.TEXT),

    /* Numeric */
    SMALLINT(ColumnType.NUMBER),
    INTEGER(ColumnType.NUMBER),
    INT(ColumnType.NUMBER),
    BIGINT(ColumnType.NUMBER),
    DECIMAL(ColumnType.NUMBER),
    NUMERIC(ColumnType.NUMBER),
    DECFLOAT(ColumnType.NUMBER),
    REAL(ColumnType.NUMBER),
    DOUBLE(ColumnType.NUMBER),

    /* Date and time */
    DATE(ColumnType.DATETIME),
    TIME(ColumnType.DATETIME),
    TIMESTAMP(ColumnType.DATETIME), ;

    private ColumnType type;

    private Db2Datatype(ColumnType type) {
        this.type = type;
    }

    public ColumnType getType() {
        return type;
    }

    public static Db2Datatype fromColumnDefinition(String sqlDefinition) {
        for (Db2Datatype datatype : values()) {
            if (sqlDefinition.toUpperCase().startsWith(datatype.name())) {
                return datatype;
            }
        }
        return null;
    }
}
