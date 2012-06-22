package info.mikaelsvensson.docutil.db2.parser;

import info.mikaelsvensson.docutil.db2.metadata.Database;

public class UnrelatedCommandHandler extends AbstractCommandHandler{
    @Override
    public void execute(Database db, String sql) {
        // Do nothing.
    }
}
