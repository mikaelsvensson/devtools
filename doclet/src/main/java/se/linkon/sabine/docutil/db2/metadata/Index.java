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

import java.util.ArrayList;
import java.util.List;

public class Index extends DatabaseObject {

    private List<String> indexColumns = new ArrayList<String>();
    private List<String> includedColumns = new ArrayList<String>();

    private boolean unique;

    public Index(String name, boolean unique, List<String> indexColumns) {
        this(name, unique, indexColumns, null);
    }

    public Index(String name, boolean unique, List<String> indexColumns, List<String> includedColumns) {
        super(name);
        setIndexColumns(indexColumns);
        setIncludedColumns(includedColumns);
        this.unique = unique;
    }

    public boolean isUnique() {
        return unique;
    }

    public List<String> getIndexColumns() {
        return indexColumns;
    }

    public void setIndexColumns(List<String> indexColumns) {
        this.indexColumns = indexColumns;
    }

    public List<String> getIncludedColumns() {
        return includedColumns;
    }

    public void setIncludedColumns(List<String> includedColumns) {
        if (includedColumns != null) {
            this.includedColumns = includedColumns;
        } else {
            this.includedColumns.clear();
        }
    }
}
