package info.mikaelsvensson.docutil.shared;

import com.sun.javadoc.Tag;

public interface InlineTagHandler {
    boolean handles(Tag tag);

    String toString(Tag tag) throws TagHandlerException;
}
