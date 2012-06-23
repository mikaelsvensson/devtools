package info.mikaelsvensson.docutil.shared.commenttext;

import com.sun.javadoc.Tag;

public interface InlineTagHandler {
    boolean handles(Tag tag);

    String toString(Tag tag) throws InlineTagHandlerException;
}
