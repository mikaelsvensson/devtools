package info.mikaelsvensson.docutil.shared.commenttext;

import com.sun.javadoc.Tag;

/** @taghandler */
public class GenericInlineTagHandler implements InlineTagHandler {

    @Override
    public boolean handles(final Tag tag) {
        return tag instanceof Tag;
    }

    @Override
    public String toString(final Tag tag) {
        if ("text".equalsIgnoreCase(tag.name())) {
            return tag.text();
        } else {
            return "<span class=\"tag-" + tag.name().substring(1) + "\">" + tag.text() + "</span>";
        }
    }

}
