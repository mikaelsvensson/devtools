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

package info.mikaelsvensson.devtools.doclet.xml.documentcreator.extensivedocumentcreator;

import com.sun.javadoc.PackageDoc;
import info.mikaelsvensson.devtools.doclet.shared.ElementWrapper;

class PackageDocHandler extends DocHandler<PackageDoc> {
// --------------------------- CONSTRUCTORS ---------------------------

    PackageDocHandler(final Dispatcher dispatcher) {
        super(PackageDoc.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final PackageDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        handleDocImpl(el, doc.allClasses(), "classes", CLASS, true);
    }
}
