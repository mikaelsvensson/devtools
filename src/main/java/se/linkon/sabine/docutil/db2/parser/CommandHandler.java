package se.linkon.sabine.docutil.db2.parser;

import se.linkon.sabine.docutil.db2.metadata.Database;

public interface CommandHandler {
    void execute(Database db, String sql);
}
