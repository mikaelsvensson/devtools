package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.SerialFieldTag;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class SerialFieldTagHandler extends DocHandler<SerialFieldTag> {

    SerialFieldTagHandler() {
        super(SerialFieldTag.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final SerialFieldTag doc) {
    }
}
