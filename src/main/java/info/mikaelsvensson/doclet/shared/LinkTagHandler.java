package info.mikaelsvensson.doclet.shared;

import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

/**
 * @taghandler
 */
public class LinkTagHandler implements TagHandler {

    @Override
    public boolean handles(final Tag tag) {
        return tag instanceof SeeTag;
    }

    @Override
    public String toString(final Tag tag) {
        SeeTag seeTag = (SeeTag) tag;
        String refClass = seeTag.referencedClass() != null ? seeTag.referencedClass().qualifiedName() : null;
        String refMember = seeTag.referencedMember() != null ? seeTag.referencedMember().name() : null;
        if (refClass != null) {
            String result = "<link class=\"" + refClass + '"';
            if (refMember != null) {
                result += " member=\"" + refMember + '"';
            }
            result += " />";
            return result;
        } else {
            return "";
        }
    }

}
