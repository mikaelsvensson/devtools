package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import info.mikaelsvensson.docutil.shared.ElementWrapper;

abstract class DocHandler<T> {
    private Class<T> handledClass;

    DocHandler(final Class<T> handledClass) {
        this.handledClass = handledClass;
    }

    void handle(final ElementWrapper el, final Object javadocObject) {
        if (handledClass.isAssignableFrom(javadocObject.getClass())) {
            handleImpl(el, (T) javadocObject);
        }
    }

    abstract void handleImpl(final ElementWrapper el, T doc);

    private final static DocHandler[] DOC_HANDLERS = {
            new AnnotationTypeDocHandler(),
            new AnnotationTypeElementDocHandler(),
            new ClassDocHandler(),
            new ConstructorDocHandler(),
            new ExecutableMemberDocHandler(),
            new FieldDocHandler(),
            new MemberDocHandler(),
            new MethodDocHandler(),
            new ParameterHandler(),
            new ParameterizedTypeHandler(),
            new ParamTagHandler(),
            new ProgramElementDocHandler(),
            new SeeTagHandler(),
            new SerialFieldTagHandler(),
            new TagHandler(),
            new ThrowsTagHandler(),
            new TypeHandler(),
            new TypeVariableHandler(),
            new WildcardTypeHandler()
    };

    public static void processRootObject(final ElementWrapper el, String elementName, final Object javadocObject) {
        if (javadocObject != null) {
            ElementWrapper child = el.addChild(elementName);
            for (DocHandler handler : DOC_HANDLERS) {
                handler.handle(child, javadocObject);
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
    public static void process(final ElementWrapper el, String elementName, final Object javadocObject) {
        if (javadocObject != null) {
            ElementWrapper child = el.addChild(elementName);

            for (DocHandler handler : DOC_HANDLERS) {
                if (!(handler instanceof ClassDocHandler)) {
                    handler.handle(child, javadocObject);
                }
            }
        }
    }

    protected void handleDocImpl(final ElementWrapper el, final Object[] javadocObjects, final String listElName, final String elName) {
        if (null != javadocObjects && javadocObjects.length > 0) {
            ElementWrapper constructorsEl = el.addChild(listElName);
            for (Object javadocObject : javadocObjects) {
                DocHandler.process(constructorsEl, elName, javadocObject);
            }
        }
    }
}
