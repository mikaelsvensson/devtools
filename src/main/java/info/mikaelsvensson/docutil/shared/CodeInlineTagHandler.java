package info.mikaelsvensson.docutil.shared;

import com.sun.javadoc.Tag;

/**
 * @taghandler
 */
public class CodeInlineTagHandler extends AbstractInlineTagHandler {

    public CodeInlineTagHandler() {
        super("code");
    }

    @Override
    public String toString(final Tag tag) {
        return "<code>" + tag.text() + "</code>";
    }

}
