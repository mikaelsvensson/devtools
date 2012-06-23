package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import info.mikaelsvensson.docutil.shared.ElementWrapper;

abstract class Handler<T> {
    private Class<T> handledClass;

    Handler(final Class<T> handledClass) {
        this.handledClass = handledClass;
    }

    boolean handle(final ElementWrapper el, final Object javadocObject) {
        if (handledClass.isAssignableFrom(javadocObject.getClass())) {
            handleImpl(el, (T) javadocObject);
            return true;
        } else {
            return false;
        }
    }

    void handleImpl(final ElementWrapper el, T doc) {
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

    public static void processRootObject(final ElementWrapper el, String elementName, final Object javadocObject) {
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
    public static ElementWrapper process(final ElementWrapper el, String elementName, final Object javadocObject) {
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

    protected void handleDocImpl(final ElementWrapper el, final Object[] javadocObjects, final String listElName, final String elName) {
        if (null != javadocObjects && javadocObjects.length > 0) {
            ElementWrapper listEl = el.addChild(listElName);
            for (Object javadocObject : javadocObjects) {
                Handler.process(listEl, elName, javadocObject);
            }
        }
    }

    protected ElementWrapper handleDocImpl(final ElementWrapper el, final Object javadocObject, final String elName) {
        if (null != javadocObject) {
            return Handler.process(el, elName, javadocObject);
        }
        return null;
    }

}
