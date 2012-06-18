package info.mikaelsvensson.docutil.db2.metadata;

public class DatabaseObject {
    private String name;

    public DatabaseObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
