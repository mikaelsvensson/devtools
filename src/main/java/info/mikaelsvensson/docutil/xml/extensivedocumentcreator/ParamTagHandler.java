package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ParamTag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ParamTagHandler extends TagHandler<ParamTag> {
// --------------------------- CONSTRUCTORS ---------------------------

    ParamTagHandler() {
        super(ParamTag.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final ParamTag doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.removeAttributes("name", "text");

        el.setAttributes(
                "type-parameter", Boolean.toString(doc.isTypeParameter()),
                "parameter-comment", doc.parameterComment(),
                "parameter-name", doc.parameterName());
    }
}
