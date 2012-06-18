package info.mikaelsvensson.docutil.shared;

import com.sun.javadoc.Tag;

import java.text.MessageFormat;

/**
 * @taghandler
 */
public class ImageTagHandler extends AbstractTagHandler {

    public ImageTagHandler() {
        super("image");
    }

    @Override
    public String toString(final Tag tag) {
        int pos = tag.text().trim().indexOf(' ');
        String src = pos > 0 ? tag.text().substring(0, pos) : tag.text();
        String alt = pos > 0 ? tag.text().substring(pos + 1) : src;
        return MessageFormat.format("<img src=\"{0}\" alt=\"{1}\" />", src, alt);
    }

}
