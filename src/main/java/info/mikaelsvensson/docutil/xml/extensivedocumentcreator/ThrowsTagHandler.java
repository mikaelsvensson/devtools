package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ThrowsTag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ThrowsTagHandler extends TagHandler<ThrowsTag> {

    ThrowsTagHandler() {
        super(ThrowsTag.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final ThrowsTag doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.remoteAttributes("name", "text");

        el.setAttributes("exception-comment", doc.exceptionComment());

        Handler.process(el, "exception-type", doc.exceptionType());
    }
}
