package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import info.mikaelsvensson.docutil.shared.ElementWrapper;

abstract class Handler<T> {
// ------------------------------ FIELDS ------------------------------

    private Dispatcher dispatcher;

    protected static final ObjectHandlerFilter ACCEPT_ALL_FILTER = new ObjectHandlerFilter() {
        @Override
        public boolean accept(final Object object) {
            return true;
        }
    };
    private Class<T> handledClass;

// -------------------------- STATIC METHODS --------------------------

    boolean handle(final ElementWrapper el, final Object javadocObject) throws JavadocItemHandlerException {
        if (handledClass.isAssignableFrom(javadocObject.getClass())) {
            handleImpl(el, (T) javadocObject);
            return true;
        } else {
            return false;
        }
    }

    void handleImpl(final ElementWrapper el, T doc) throws JavadocItemHandlerException {
    }

// --------------------------- CONSTRUCTORS ---------------------------

    Handler(final Class<T> handledClass, final Dispatcher dispatcher) {
        this.handledClass = handledClass;
        this.dispatcher = dispatcher;
    }

// -------------------------- OTHER METHODS --------------------------

    protected ElementWrapper handleDocImpl(final ElementWrapper el, final Object javadocObject, final String elName) throws JavadocItemHandlerException {
        if (null != javadocObject) {
            return handleDocImpl(el, elName, javadocObject);
        }
        return null;
    }

    protected void handleDocImpl(final ElementWrapper el, final Object[] javadocObjects, final String listElName, final String elName) throws JavadocItemHandlerException {
        handleDocImpl(el, javadocObjects, ACCEPT_ALL_FILTER, listElName, elName);
    }

    protected void handleDocImpl(final ElementWrapper el, final Object[] javadocObjects, final String listElName, final String elName, final boolean includeClassHandler) throws JavadocItemHandlerException {
        handleDocImpl(el, javadocObjects, ACCEPT_ALL_FILTER, listElName, elName, includeClassHandler);
    }

    protected <X> void handleDocImpl(final ElementWrapper el, final X[] javadocObjects, final ObjectHandlerFilter<X> filter, final String listElName, final String elName) throws JavadocItemHandlerException {
        handleDocImpl(el, javadocObjects, filter, listElName, elName, false);
    }

    protected <X> void handleDocImpl(final ElementWrapper el, final X[] javadocObjects, final ObjectHandlerFilter<X> filter, final String listElName, final String elName, final boolean includeClassHandler) throws JavadocItemHandlerException {
        if (null != javadocObjects && javadocObjects.length > 0) {
            ElementWrapper listEl = el.addChild(listElName);
            for (X javadocObject : javadocObjects) {
                if (filter.accept(javadocObject)) {
                    handleDocImpl(listEl, elName, javadocObject, includeClassHandler);
                }
            }
        }
    }

    protected <X> ElementWrapper handleDocImpl(final ElementWrapper el, final String elName, final X javadocObject) throws JavadocItemHandlerException {
        return dispatcher.dispatch(el, elName, javadocObject);
    }

    protected <X> ElementWrapper handleDocImpl(final ElementWrapper el, final String elName, final X javadocObject, final boolean includeClassHandler) throws JavadocItemHandlerException {
        return dispatcher.dispatch(el, elName, javadocObject, includeClassHandler);
    }
    protected String getProperty(final String property) {
        return dispatcher.getProperty(property);
    }

}
