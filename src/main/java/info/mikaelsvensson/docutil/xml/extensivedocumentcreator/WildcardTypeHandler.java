package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.WildcardType;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class WildcardTypeHandler extends TypeHandler<WildcardType> {
// --------------------------- CONSTRUCTORS ---------------------------

    WildcardTypeHandler() {
        super(WildcardType.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final WildcardType doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        handleDocImpl(el, doc.extendsBounds(), "extends-bounds", "extends-bound");

        handleDocImpl(el, doc.superBounds(), "super-bounds", "super-bound");
    }
}
