package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Tag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class TagHandler extends DocHandler<Tag> {

    TagHandler() {
        super(Tag.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final Tag doc) {
        el.setAttributes(
                "name", doc.name(),
                "kind", doc.kind(),
                "text", doc.text());

//            handleDocImpl(el, doc.inlineTags(), "inline-tags", "inline-tag");
    }
}
