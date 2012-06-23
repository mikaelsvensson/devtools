package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ExecutableMemberDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

class ExecutableMemberDocHandler extends DocHandler<ExecutableMemberDoc> {

    ExecutableMemberDocHandler() {
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
