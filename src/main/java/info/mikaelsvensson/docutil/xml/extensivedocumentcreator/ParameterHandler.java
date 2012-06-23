package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Parameter;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ParameterHandler extends Handler<Parameter> {
// --------------------------- CONSTRUCTORS ---------------------------

    ParameterHandler(final Dispatcher dispatcher) {
        super(Parameter.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final Parameter doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes("name", doc.name());

        handleDocImpl(el, "type", doc.type());
    }
}
