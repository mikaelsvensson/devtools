package info.mikaelsvensson.docutil;

import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;

/**
 * Image with {@image resources/cloud.png} or without {@image resources/cloud.png A cloud} alternative texts.
 */
public class AbstractDoclet {

/*
    public RootDoc getRoot() {
        return root;
    }
*/

    protected RootDoc root;

    protected AbstractDoclet(RootDoc root) {
        this.root = root;
    }

    protected String readOption(String optionName) {
        return readOption(optionName, root);
    }

    protected static String readOption(String optionName, RootDoc root) {
        String[][] options = root.options();
        for (String[] opt : options) {
            if (opt[0].equals(optionName)) {
                return opt[1];
            }
        }
        return null;
    }

    /**
     * Indicate that this doclet supports the 1.5 language features.
     * <p/>
     * Credits to http://stackoverflow.com/questions/5731619/doclet-get-generics-of-a-list
     *
     * @return JAVA_1_5, indicating that the new features are supported.
     */
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

    protected void printNotice(String message) {
        root.printNotice(message);
    }

    protected void printError(Exception e) {
        root.printError(e.getMessage());
    }

    protected void printWarning(Exception e) {
        root.printWarning(e.getMessage());
    }

}
