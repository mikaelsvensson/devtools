package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ThrowsTag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ThrowsTagHandler extends DocHandler<ThrowsTag> {

    ThrowsTagHandler() {
        super(ThrowsTag.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final ThrowsTag doc) {
        el.setAttributes("exception-comment", doc.exceptionComment());

        DocHandler.process(el, "exception-type", doc.exceptionType());
    }
}
