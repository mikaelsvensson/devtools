package info.mikaelsvensson.doclet.shared;

import com.sun.javadoc.Tag;

public abstract class AbstractTagHandler implements TagHandler {
    private String name;

    public AbstractTagHandler(final String name) {
        this.name = name;
    }

    @Override
    public boolean handles(final Tag tag) {
        return tag.kind().equals("@" + name);
    }

}
