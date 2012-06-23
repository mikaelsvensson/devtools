package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Type;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ClassDocHandler extends DocHandler<ClassDoc> {

    ClassDocHandler() {
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
