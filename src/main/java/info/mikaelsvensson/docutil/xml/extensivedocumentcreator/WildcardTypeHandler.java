package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.WildcardType;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class WildcardTypeHandler extends DocHandler<WildcardType> {

    WildcardTypeHandler() {
        super(WildcardType.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final WildcardType doc) {
        handleDocImpl(el, doc.extendsBounds(), "extends-bounds", "extends-bound");

        handleDocImpl(el, doc.superBounds(), "super-bounds", "super-bound");
    }
}
