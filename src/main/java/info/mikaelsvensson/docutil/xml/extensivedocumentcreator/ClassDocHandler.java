package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ClassDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ClassDocHandler<T extends ClassDoc> extends ProgramElementDocHandler<T> {

    ClassDocHandler() {
        super((Class<T>) ClassDoc.class);
    }

    protected ClassDocHandler(final Class<T> cls) {
        super(cls);
    }

    @Override
    void handleImpl(final ElementWrapper el, final T doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.setAttributes(
                "abstract", Boolean.toString(doc.isAbstract()),
                "externalizable", Boolean.toString(doc.isExternalizable()),
                "serializable", Boolean.toString(doc.isSerializable()));

        Handler.process(el, "superclass", doc.superclassType());

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
