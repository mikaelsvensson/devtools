package info.mikaelsvensson.docutil.chain;

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
