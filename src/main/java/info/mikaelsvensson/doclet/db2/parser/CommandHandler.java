package info.mikaelsvensson.doclet.db2.parser;

import info.mikaelsvensson.doclet.db2.metadata.Database;

public interface CommandHandler {
    void execute(Database db, String sql);
}
