package info.mikaelsvensson.doclet.shared;

import com.sun.javadoc.Tag;

/**
 * @taghandler
 */
public class CodeTagHandler extends AbstractTagHandler {

    public CodeTagHandler() {
        super("code");
    }

    @Override
    public String toString(final Tag tag) {
        return "<code>" + tag.text() + "</code>";
    }

}
