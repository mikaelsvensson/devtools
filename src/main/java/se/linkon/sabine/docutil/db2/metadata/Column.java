package se.linkon.sabine.docutil.db2.metadata;

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
