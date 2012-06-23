package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Parameter;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ParameterHandler extends DocHandler<Parameter> {

    ParameterHandler() {
        super(Parameter.class);
    }

    @Override
    void handleImpl(final ElementWrapper el, final Parameter doc) {
        el.setAttributes("name", doc.name());

        DocHandler.process(el, "type", doc.type());
    }
}
