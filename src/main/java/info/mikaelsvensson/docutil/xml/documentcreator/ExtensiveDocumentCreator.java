package info.mikaelsvensson.docutil.xml.documentcreator;

import com.sun.javadoc.*;
import info.mikaelsvensson.docutil.shared.DocumentCreatorException;
import info.mikaelsvensson.docutil.shared.DocumentWrapper;
import info.mikaelsvensson.docutil.shared.ElementWrapper;
import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public class ExtensiveDocumentCreator extends AbstractDocumentCreator {

    public static final String NAME = "extensive";

    private abstract static class DocHandler<T> {
        private Class<T> handledClass;

        protected DocHandler(final Class<T> handledClass) {
            this.handledClass = handledClass;
        }

        void handle(final ElementWrapper el, final Object javadocObject) {
            if (null == handledClass || javadocObject == null) {
                System.out.println("Oops");
            }
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

    private static class ProgramElementDocHandler extends DocHandler<ProgramElementDoc> {

        protected ProgramElementDocHandler() {
            super(ProgramElementDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final ProgramElementDoc doc) {
            el.setAttributes(
//                    "qualified-name", doc.qualifiedName(),
                    "name", doc.name(),
                    "final", Boolean.toString(doc.isFinal()),
                    "access", getAccess(doc),
                    "static", Boolean.toString(doc.isStatic()));

            handleDocImpl(el, doc.annotations(), "annotations", "annotation");
        }

        private String getAccess(ProgramElementDoc javadocItem) {
            if (javadocItem.isProtected()) {
                return "package";
            } else if (javadocItem.isPrivate()) {
                return "private";
            } else if (javadocItem.isPublic()) {
                return "public";
            } else {
                return "default";
            }
        }
    }

    private static class ClassDocHandler extends DocHandler<ClassDoc> {

        protected ClassDocHandler() {
            super(ClassDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final ClassDoc doc) {
            if (doc.getClass().getName().equals(Type.class.getName())) {
                return;
            }
            el.setAttributes(
                    "abstract", Boolean.toString(doc.isAbstract()),
                    "externalizable", Boolean.toString(doc.isExternalizable()),
                    "serializable", Boolean.toString(doc.isSerializable()));

            DocHandler.process(el, "superclass", doc.superclassType());

            handleDocImpl(el, doc.constructors(), "constructors", "constructor");

            handleDocImpl(el, doc.enumConstants(), "enum-constants", "enum-constant");

            handleDocImpl(el, doc.fields(), "fields", "field");

            handleDocImpl(el, doc.innerClasses(), "inner-classes", "inner-class");

            handleDocImpl(el, doc.interfaceTypes(), "interfaces", "interface");

            handleDocImpl(el, doc.methods(), "methods", "method");

            handleDocImpl(el, doc.typeParameters(), "type-parameters", "type-parameter");

            handleDocImpl(el, doc.typeParamTags(), "type-parameter-tags", "type-parameter-tag");
        }
    }

    private static class AnnotationTypeDocHandler extends DocHandler<AnnotationTypeDoc> {

        protected AnnotationTypeDocHandler() {
            super(AnnotationTypeDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final AnnotationTypeDoc doc) {
            handleDocImpl(el, doc.elements(), "elements", "element");
        }
    }

    private static class MemberDocHandler extends DocHandler<MemberDoc> {

        protected MemberDocHandler() {
            super(MemberDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final MemberDoc doc) {
            el.setAttributes("synthetic", Boolean.toString(doc.isSynthetic()));
        }
    }

    private static class ExecutableMemberDocHandler extends DocHandler<ExecutableMemberDoc> {

        protected ExecutableMemberDocHandler() {
            super(ExecutableMemberDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final ExecutableMemberDoc doc) {
            el.setAttributes(
                    "native", Boolean.toString(doc.isNative()),
                    "synchronized", Boolean.toString(doc.isSynchronized()),
                    "varargs", Boolean.toString(doc.isVarArgs()));

            handleDocImpl(el, doc.parameters(), "parameters", "parameter");

            handleDocImpl(el, doc.paramTags(), "parameter-tags", "parameter-tag");

            handleDocImpl(el, doc.thrownExceptionTypes(), "thrown-exceptions", "thrown-exception");

            handleDocImpl(el, doc.throwsTags(), "throws-tags", "throws-tags");

            handleDocImpl(el, doc.typeParameters(), "type-parameters", "type-parameter");

            handleDocImpl(el, doc.typeParamTags(), "type-parameter-tags", "type-parameter-tag");
        }
    }

    private static class MethodDocHandler extends DocHandler<MethodDoc> {

        protected MethodDocHandler() {
            super(MethodDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final MethodDoc doc) {
            el.setAttributes("abstract", Boolean.toString(doc.isAbstract()));

            DocHandler.process(el, "returns", doc.returnType());

            DocHandler.process(el, "overrides", doc.overriddenType());
        }
    }

    private static class FieldDocHandler extends DocHandler<FieldDoc> {

        protected FieldDocHandler() {
            super(FieldDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final FieldDoc doc) {
            el.setAttributes(
                    "transient", Boolean.toString(doc.isTransient()),
                    "volatile", Boolean.toString(doc.isVolatile())
            );

            if (doc.constantValue() != null) {
                el.setAttribute("constant-value", doc.constantValue().toString());
            }

            DocHandler.process(el, "type", doc.type());

            handleDocImpl(el, doc.serialFieldTags(), "serial-field-tags", "serial-field-tag");
        }
    }

    private static class AnnotationTypeElementDocHandler extends DocHandler<AnnotationTypeElementDoc> {

        protected AnnotationTypeElementDocHandler() {
            super(AnnotationTypeElementDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final AnnotationTypeElementDoc doc) {
        }
    }

    private static class ConstructorDocHandler extends DocHandler<ConstructorDoc> {

        protected ConstructorDocHandler() {
            super(ConstructorDoc.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final ConstructorDoc doc) {
        }
    }

    private static class SerialFieldTagHandler extends DocHandler<SerialFieldTag> {

        protected SerialFieldTagHandler() {
            super(SerialFieldTag.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final SerialFieldTag doc) {
        }
    }

    private static class ParameterHandler extends DocHandler<Parameter> {

        protected ParameterHandler() {
            super(Parameter.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final Parameter doc) {
            el.setAttributes("name", doc.name());

            DocHandler.process(el, "type", doc.type());
        }
    }

    private static class TagHandler extends DocHandler<Tag> {

        protected TagHandler() {
            super(Tag.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final Tag doc) {
            el.setAttributes(
                    "name", doc.name(),
                    "kind", doc.kind(),
                    "text", doc.text());

//            handleDocImpl(el, doc.inlineTags(), "inline-tags", "inline-tag");
        }
    }

    private static class ParamTagHandler extends DocHandler<ParamTag> {

        protected ParamTagHandler() {
            super(ParamTag.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final ParamTag doc) {
            el.setAttributes(
                    "type-parameter", Boolean.toString(doc.isTypeParameter()),
                    "parameter-comment", doc.parameterComment(),
                    "parameter-name", doc.parameterName());
        }
    }

    private static class SeeTagHandler extends DocHandler<SeeTag> {

        protected SeeTagHandler() {
            super(SeeTag.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final SeeTag doc) {
            el.setAttributes(
                    "label", doc.label(),
                    "referenced-class", doc.referencedClassName(),
                    "referenced-member", doc.referencedMemberName(),
                    "referenced-package", doc.referencedPackage().name());
        }
    }

    private static class ThrowsTagHandler extends DocHandler<ThrowsTag> {

        protected ThrowsTagHandler() {
            super(ThrowsTag.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final ThrowsTag doc) {
            el.setAttributes("exception-comment", doc.exceptionComment());

            DocHandler.process(el, "exception-type", doc.exceptionType());
        }
    }

    private static class TypeHandler extends DocHandler<Type> {

        protected TypeHandler() {
            super(Type.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final Type doc) {
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

    private static class ParameterizedTypeHandler extends DocHandler<ParameterizedType> {

        protected ParameterizedTypeHandler() {
            super(ParameterizedType.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final ParameterizedType doc) {
            DocHandler.process(el, "superclass-type", doc.superclassType());

            handleDocImpl(el, doc.typeArguments(), "type-arguments", "type-argument");

            handleDocImpl(el, doc.interfaceTypes(), "interfaces", "interface");
        }
    }

    private static class TypeVariableHandler extends DocHandler<TypeVariable> {

        protected TypeVariableHandler() {
            super(TypeVariable.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final TypeVariable doc) {
            handleDocImpl(el, doc.bounds(), "bounds", "bound");
        }
    }

    private static class WildcardTypeHandler extends DocHandler<WildcardType> {

        protected WildcardTypeHandler() {
            super(WildcardType.class);
        }

        @Override
        void handleImpl(final ElementWrapper el, final WildcardType doc) {
            handleDocImpl(el, doc.extendsBounds(), "extends-bounds", "extends-bound");

            handleDocImpl(el, doc.superBounds(), "super-bounds", "super-bound");
        }
    }

    @Override
    public Document generateDocument(final RootDoc doc, final PropertySet properties) throws DocumentCreatorException {
        DocumentWrapper dw = null;
        try {
            dw = new DocumentWrapper(createDocument("javadoc"));
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException(e);
        }

        for (ClassDoc classDoc : doc.classes()) {
            DocHandler.processRootObject(dw, "class", (Doc) classDoc);
        }

        return dw.getDocument();
    }

}
