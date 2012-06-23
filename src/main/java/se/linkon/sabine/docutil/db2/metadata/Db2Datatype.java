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

package se.linkon.sabine.docutil.db2.metadata;

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
