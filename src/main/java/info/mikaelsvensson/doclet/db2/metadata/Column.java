package info.mikaelsvensson.doclet.db2.metadata;

public class Column extends DatabaseObject{
    private String definition;

    public Column(String name, String definition) {
        super(name);
        this.definition = definition;
    }

    public Column(String name) {
        super(name);
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
