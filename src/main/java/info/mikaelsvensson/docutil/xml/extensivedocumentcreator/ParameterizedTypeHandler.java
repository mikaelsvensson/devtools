package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ParameterizedType;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ParameterizedTypeHandler extends TypeHandler<ParameterizedType> {
// --------------------------- CONSTRUCTORS ---------------------------

    ParameterizedTypeHandler() {
        super(ParameterizedType.class);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final ParameterizedType doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        Handler.process(el, "superclass-type", doc.superclassType());

        handleDocImpl(el, doc.typeArguments(), "type-arguments", "type-argument");

//        handleDocImpl(el, doc.interfaceTypes(), "interfaces", "interface");
    }
}
