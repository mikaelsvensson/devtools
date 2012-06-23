package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import info.mikaelsvensson.docutil.shared.ElementWrapper;

abstract class Handler<T> {
    protected static final ObjectHandlerFilter ACCEPT_ALL_FILTER = new ObjectHandlerFilter() {
        @Override
        public boolean accept(final Object object) {
            return true;
        }
    };
    private Class<T> handledClass;

    Handler(final Class<T> handledClass) {
        this.handledClass = handledClass;
    }

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

    private final static Handler[] HANDLERS = {
            /**/ new AnnotationTypeElementDocHandler(),
            /*  */ new MethodDocHandler(),
            /*  */ new ConstructorDocHandler(),
            /*    */ new ExecutableMemberDocHandler(),
            /*    */ new FieldDocHandler(),
            /*      */ new MemberDocHandler(),
            /*      */ new ClassDocHandler(),
            /*        */ new ProgramElementDocHandler(),
            /*        */ new PackageDocHandler(),
            /*          */ new DocHandler(),

            /**/ new ParameterHandler(),

            /**/ new ParamTagHandler(),
            /**/ new SeeTagHandler(),
            /**/ new ThrowsTagHandler(),
            /**/ new SerialFieldTagHandler(),
            /*  */ new TagHandler(),

            /**/ new AnnotationTypeDocHandler(),
            /**/ new ParameterizedTypeHandler(),
            /**/ new TypeVariableHandler(),
            /**/ new WildcardTypeHandler(),
            /*  */ new TypeHandler()
    };

    public static void processRootObject(final ElementWrapper el, String elementName, final Object javadocObject) throws JavadocItemHandlerException {
        if (javadocObject != null) {
            ElementWrapper child = el.addChild(elementName);
            for (Handler handler : HANDLERS) {
                if (handler.handle(child, javadocObject)) {
                    break;
                }
            }
        }
    }

    /*

            public static void process(final ElementWrapper el, final AnnotationDesc annotationDesc) {
    //            throw new NotImplementedException();
            }

            public static void process(final ElementWrapper el, final AnnotationDesc.ElementValuePair elementValuePair) {
    //            throw new NotImplementedException();
            }

            public static void process(final ElementWrapper el, final AnnotationValue annotationValue) {
    //            throw new NotImplementedException();
            }

            public static void process(final ElementWrapper el, final Parameter parameter) {
    //            throw new NotImplementedException();
            }

            public static void process(final ElementWrapper el, final Tag tag) {
    //            throw new NotImplementedException();
            }

    */
    public static ElementWrapper process(final ElementWrapper el, String elementName, final Object javadocObject) throws JavadocItemHandlerException {
        if (javadocObject != null) {
            ElementWrapper child = el.addChild(elementName);

            for (Handler handler : HANDLERS) {
                if (!(handler instanceof ClassDocHandler)) {
                    if (handler.handle(child, javadocObject)) {
                        break;
                    }
                }
            }

            return child;
        }
        return null;
    }

    protected void handleDocImpl(final ElementWrapper el, final Object[] javadocObjects, final String listElName, final String elName) throws JavadocItemHandlerException {
        handleDocImpl(el, javadocObjects, ACCEPT_ALL_FILTER, listElName, elName);
    }

    protected <X> void handleDocImpl(final ElementWrapper el, final X[] javadocObjects, final ObjectHandlerFilter<X> filter, final String listElName, final String elName) throws JavadocItemHandlerException {
        if (null != javadocObjects && javadocObjects.length > 0) {
            ElementWrapper listEl = el.addChild(listElName);
            for (X javadocObject : javadocObjects) {
                if (filter.accept(javadocObject)) {
                    Handler.process(listEl, elName, javadocObject);
                }
            }
        }
    }

    protected ElementWrapper handleDocImpl(final ElementWrapper el, final Object javadocObject, final String elName) throws JavadocItemHandlerException {
        if (null != javadocObject) {
            return Handler.process(el, elName, javadocObject);
        }
        return null;
    }

}
