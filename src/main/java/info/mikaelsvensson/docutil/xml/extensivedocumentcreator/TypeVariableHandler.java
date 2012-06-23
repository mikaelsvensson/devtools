package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.TypeVariable;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class TypeVariableHandler extends TypeHandler<TypeVariable> {
// --------------------------- CONSTRUCTORS ---------------------------

    TypeVariableHandler(final Dispatcher dispatcher) {
        super(TypeVariable.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final TypeVariable doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        handleDocImpl(el, doc.bounds(), "bounds", "bound");
    }
}
