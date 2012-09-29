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

package info.mikaelsvensson.doctools.chain;

import com.sun.javadoc.*;

public class DocRootWrapper implements RootDoc {
    private RootDoc root;

    @Override
    public ClassDoc[] classes() {
        return root.classes();
    }

    @Override
    public ClassDoc classNamed(final String s) {
        return root.classNamed(s);
    }

    @Override
    public String[][] options() {
        return root.options();
    }

    @Override
    public PackageDoc packageNamed(final String s) {
        return root.packageNamed(s);
    }

    @Override
    public ClassDoc[] specifiedClasses() {
        return root.specifiedClasses();
    }

    @Override
    public PackageDoc[] specifiedPackages() {
        return root.specifiedPackages();
    }

    @Override
    public String commentText() {
        return root.commentText();
    }

    @Override
    public int compareTo(final Object o) {
        return root.compareTo(o);
    }

    @Override
    public Tag[] firstSentenceTags() {
        return root.firstSentenceTags();
    }

    @Override
    public String getRawCommentText() {
        return root.getRawCommentText();
    }

    @Override
    public Tag[] inlineTags() {
        return root.inlineTags();
    }

    @Override
    public boolean isAnnotationType() {
        return root.isAnnotationType();
    }

    @Override
    public boolean isAnnotationTypeElement() {
        return root.isAnnotationTypeElement();
    }

    @Override
    public boolean isClass() {
        return root.isClass();
    }

    @Override
    public boolean isConstructor() {
        return root.isConstructor();
    }

    @Override
    public boolean isEnum() {
        return root.isEnum();
    }

    @Override
    public boolean isEnumConstant() {
        return root.isEnumConstant();
    }

    @Override
    public boolean isError() {
        return root.isError();
    }

    @Override
    public boolean isException() {
        return root.isException();
    }

    @Override
    public boolean isField() {
        return root.isField();
    }

    @Override
    public boolean isIncluded() {
        return root.isIncluded();
    }

    @Override
    public boolean isInterface() {
        return root.isInterface();
    }

    @Override
    public boolean isMethod() {
        return root.isMethod();
    }

    @Override
    public boolean isOrdinaryClass() {
        return root.isOrdinaryClass();
    }

    @Override
    public String name() {
        return root.name();
    }

    @Override
    public SourcePosition position() {
        return root.position();
    }

    @Override
    public SeeTag[] seeTags() {
        return root.seeTags();
    }

    @Override
    public void setRawCommentText(final String s) {
        root.setRawCommentText(s);
    }

    @Override
    public Tag[] tags() {
        return root.tags();
    }

    @Override
    public Tag[] tags(final String s) {
        return root.tags(s);
    }

    @Override
    public void printError(final SourcePosition sourcePosition, final String s) {
        root.printError(sourcePosition, s);
    }

    @Override
    public void printError(final String s) {
        root.printError(s);
    }

    @Override
    public void printNotice(final SourcePosition sourcePosition, final String s) {
        root.printNotice(sourcePosition, s);
    }

    @Override
    public void printNotice(final String s) {
        root.printNotice(s);
    }

    @Override
    public void printWarning(final SourcePosition sourcePosition, final String s) {
        root.printWarning(sourcePosition, s);
    }

    @Override
    public void printWarning(final String s) {
        root.printWarning(s);
    }

    public DocRootWrapper(final RootDoc root) {
        this.root = root;
    }
}
