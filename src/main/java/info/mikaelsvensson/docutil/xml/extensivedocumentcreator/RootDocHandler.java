package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

import java.util.HashSet;
import java.util.Set;

class RootDocHandler extends DocHandler<RootDoc> {
// ------------------------------ FIELDS ------------------------------

// --------------------------- CONSTRUCTORS ---------------------------

    RootDocHandler(final Dispatcher dispatcher) {
        super(RootDoc.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final RootDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.removeAttributes(NAME);

        el.setAttribute("message-from-developer", getProperty("msg"));

        Set<PackageDoc> packages = new HashSet<PackageDoc>();
        for (ClassDoc classDoc : doc.classes()) {
            packages.add(classDoc.containingPackage());
        }

        handleDocImpl(el, packages.toArray(new PackageDoc[]{}), "packages", "package");
    }

}
