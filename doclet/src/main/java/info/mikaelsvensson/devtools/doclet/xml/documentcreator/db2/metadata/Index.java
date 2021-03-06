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

package info.mikaelsvensson.devtools.doclet.xml.documentcreator.db2.metadata;

import java.util.ArrayList;
import java.util.List;

public class Index extends DatabaseObject {

    private List<String> columns = new ArrayList<String>();

    private boolean unique;

    public Index(String name, boolean unique, List<String> columns) {
        super(name);
        setColumns(columns);
        this.unique = unique;
    }

    public List<String> getColumns() {
        return columns;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void addColumn(String column) {
        this.columns.add(column);
    }
}
