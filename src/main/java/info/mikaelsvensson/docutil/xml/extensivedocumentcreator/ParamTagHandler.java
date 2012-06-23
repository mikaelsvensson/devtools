package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ParamTag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ParamTagHandler extends DocHandler<ParamTag> {

    ParamTagHandler() {
        super(ParamTag.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final ParamTag doc) {
        el.setAttributes(
                "type-parameter", Boolean.toString(doc.isTypeParameter()),
                "parameter-comment", doc.parameterComment(),
                "parameter-name", doc.parameterName());
    }
}
