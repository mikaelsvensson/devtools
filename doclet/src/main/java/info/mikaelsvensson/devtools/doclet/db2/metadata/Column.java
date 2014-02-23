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

public class Column extends DatabaseObject {
    private String definition;
    private Db2Datatype db2Datatype;

    public Column(String name, String definition) {
        super(name);
        setDefinition(definition);
    }

    public Column(String name) {
        super(name);
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        int posSpace = definition.indexOf(' ');
        if (posSpace == -1) {
            posSpace = definition.length();
        }
        String datatypeDef = definition.substring(0, posSpace);
        this.db2Datatype = Db2Datatype.fromColumnDefinition(datatypeDef);
        if (this.db2Datatype != null) {
            this.definition = definition.substring(posSpace).trim();
        } else {
            this.definition = definition;
        }
    }

    public Db2Datatype getDb2Datatype() {
        return db2Datatype;
    }
}
