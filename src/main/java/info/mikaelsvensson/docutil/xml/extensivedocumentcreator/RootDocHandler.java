/*
 * Copyright (c) 2012, Mikael Svensson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the copyright holder nor the names of the
 *       contributors of this software may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package info.mikaelsvensson.docutil.xml.extensivedocumentcreator;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.docutil.shared.ElementWrapper;

import java.util.HashSet;
import java.util.Set;

class RootDocHandler extends DocHandler<RootDoc> /*implements ClassDocHandler.Callback*/ {
// --------------------------- CONSTRUCTORS ---------------------------

    RootDocHandler(final Dispatcher dispatcher) {
        super(RootDoc.class, dispatcher);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    void handleImpl(final ElementWrapper el, final RootDoc doc) throws JavadocItemHandlerException {
        super.handleImpl(el, doc);

        el.removeAttributes(NAME);

//        el.addChild("packages");
//        for (ClassDoc classDoc : doc.classes()) {
//            ElementWrapper pkgEl = getElementForPackage(el, classDoc.containingPackage());
//            handleDocImpl(pkgEl, classDoc, "class");
//        }


        Set<PackageDoc> packages = new HashSet<PackageDoc>();
        for (ClassDoc classDoc : doc.classes()) {
            packages.add(classDoc.containingPackage());
        }
        handleDocImpl(el, packages.toArray(new PackageDoc[]{}), "packages", "package");
    }

//    private Set<String> processedClasses = new HashSet<String>();

//    private Map<PackageDoc, ElementWrapper> packageMap = new HashMap<PackageDoc, ElementWrapper>();
//    private Map<ClassDoc, ElementWrapper> classMap = new HashMap<ClassDoc, ElementWrapper>();

/*
    @Override
    public void onTrigger(final ClassDoc doc) {
        if (!processedClasses.contains(doc.qualifiedName())) {
            processedClasses.add(doc.qualifiedName());
            detailedClassDocHandler.handle()
        }
    }
*/

/*
    public static ElementWrapper getElementForClass(final ElementWrapper rootElement, final ClassDoc doc) {
        ElementWrapper packageEl = getElementForPackage(rootElement, doc.containingPackage());
        for (ElementWrapper cls : packageEl.getChildren("class")) {
            if (cls.getAttribute(ProgramElementDocHandler.QUALIFIED_NAME).equals(doc.qualifiedName())) {
                return cls;
            }
        }
        return packageEl.addChild("class");
    }
*/

/*
    private static ElementWrapper getElementForPackage(final ElementWrapper elementWrapper, final PackageDoc packageDoc) {
        ElementWrapper packageEl = null;
        ElementWrapper packagesEl = elementWrapper.getChildren("packages")[0];
        for (ElementWrapper pkg : packagesEl.getChildren("package")) {
            if (pkg.getAttribute("name").equals(packageDoc.name())) {
                return pkg;
            }
        }
//        if (null == packageEl) {
            return packagesEl.addChild("package", "name", packageDoc.name());
//        }
//        return packageEl;
    }
*/
}
