package info.mikaelsvensson.docutil.shared;

import com.sun.javadoc.Tag;

public abstract class AbstractInlineTagHandler implements InlineTagHandler {
    private String name;

    public AbstractInlineTagHandler(final String name) {
        this.name = name;
    }

    @Override
    public boolean handles(final Tag tag) {
        return tag.kind().equals("@" + name);
    }

}
