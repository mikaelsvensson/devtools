package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.Type;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class TypeHandler<T extends Type> extends Handler<T> {
// --------------------------- CONSTRUCTORS ---------------------------

    TypeHandler(final Dispatcher dispatcher) {
        super((Class<T>) Type.class, dispatcher);
    }

    public TypeHandler(final Class<T> docClass, final Dispatcher dispatcher) {
        super(docClass, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                "dimension", Integer.toString(getDimensionCount(doc.dimension())),
                "primitive", Boolean.toString(doc.isPrimitive()),
                "qualified-name", doc.qualifiedTypeName());
    }

    private int getDimensionCount(final String dimension) {
        int count = 0;
        int pos = -1;
        while ((pos = dimension.indexOf('[',pos+1)) != -1) {
            count++;
        }
        return count;
    }
}
