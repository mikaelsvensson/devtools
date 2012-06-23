package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;

public class Dispatcher {
// ------------------------------ FIELDS ------------------------------

    private PropertySet propertySet;

    private Handler[] handlers;

    public Dispatcher(final PropertySet propertySet) {
        this.propertySet = propertySet;
        handlers = new Handler[] {
                /**/ new AnnotationTypeElementDocHandler(this),
                /*  */ new MethodDocHandler(this),
                /*  */ new ConstructorDocHandler(this),
                /*    */ new ExecutableMemberDocHandler(this),
                /*    */ new FieldDocHandler(this),
                /*      */ new MemberDocHandler(this),
                /*      */ new ClassDocHandler(this),
                /*        */ new ProgramElementDocHandler(this),
                /*        */ new RootDocHandler(this),
                /*        */ new PackageDocHandler(this),
                /*          */ new DocHandler(this),

                /**/ new ParameterHandler(this),

                /**/ new ParamTagHandler(this),
                /**/ new SeeTagHandler(this),
                /**/ new ThrowsTagHandler(this),
                /**/ new SerialFieldTagHandler(this),
                /*  */ new TagHandler(this),

                /**/ new AnnotationTypeDocHandler(this),
                /**/ new ParameterizedTypeHandler(this),
                /**/ new TypeVariableHandler(this),
                /**/ new WildcardTypeHandler(this),
                /*  */ new TypeHandler(this)
        };
    }

    public String getProperty(final String key) {
        return propertySet.getProperty(key);
    }

    public ElementWrapper dispatch(final ElementWrapper el, String elementName, final Object javadocObject) throws JavadocItemHandlerException {
        return dispatch(el, elementName, javadocObject, false);
    }

    public ElementWrapper dispatch(final ElementWrapper el, String elementName, final Object javadocObject, final boolean includeClassHandler) throws JavadocItemHandlerException {
        if (javadocObject != null) {
            ElementWrapper child = el.addChild(elementName);

            for (Handler handler : handlers) {
                if (includeClassHandler || !(handler instanceof ClassDocHandler)) {
                    if (handler.handle(child, javadocObject)) {
                        break;
                    }
                }
            }

            return child;
        }
        return null;
    }
}
