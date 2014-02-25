/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.doclet.xml.documentcreator.extensive;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

import java.util.HashSet;
import java.util.Set;

class RootDocHandler extends DocHandler<RootDoc> {
// --------------------------- CONSTRUCTORS ---------------------------

    RootDocHandler(final Dispatcher dispatcher) {
        super(RootDoc.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final RootDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.removeAttributes(NAME);

        Set<PackageDoc> packages = new HashSet<PackageDoc>();
        for (ClassDoc classDoc : doc.classes()) {
            packages.add(classDoc.containingPackage());
        }
        handleDocImpl(el, packages.toArray(new PackageDoc[packages.size()]), "packages", "package");
    }
}
