package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Parameter;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ParameterHandler extends Handler<Parameter> {

    ParameterHandler() {
        super(Parameter.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final Parameter doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes("name", doc.name());

        Handler.process(el, "type", doc.type());
    }
}
