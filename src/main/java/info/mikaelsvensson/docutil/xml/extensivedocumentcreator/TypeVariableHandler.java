package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.TypeVariable;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class TypeVariableHandler extends TypeHandler<TypeVariable> {

    TypeVariableHandler() {
        super(TypeVariable.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final TypeVariable doc) {
        super.handleImpl(el, doc);

        handleDocImpl(el, doc.bounds(), "bounds", "bound");
    }
}
