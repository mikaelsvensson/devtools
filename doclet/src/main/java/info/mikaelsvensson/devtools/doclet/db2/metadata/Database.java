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

import java.util.*;

public class Database extends DatabaseObject {
    private List<Table> tables = new ArrayList<Table>();
    private Map<String, String> properties = new TreeMap<String, String>();
    public Date getTimeStamp() {
        return timeStamp;
    }

    private Date timeStamp;

    public List<Table> getTables() {
        return tables;
    }

    public boolean addTable(Table table) {
        return tables.add(table);
    }

    public Database() {
        super("Untitled");
    }

    public Table getTable(String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public void setTimeStamp(Date date) {
        this.timeStamp = date;
    }
}
