package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.SerialFieldTag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class SerialFieldTagHandler extends TagHandler<SerialFieldTag> {
// --------------------------- CONSTRUCTORS ---------------------------

    SerialFieldTagHandler() {
        super(SerialFieldTag.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final SerialFieldTag doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);
    }
}
