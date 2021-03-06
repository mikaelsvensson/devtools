package info.mikaelsvensson.devtools.chain;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

public class TestDocletAlice {
// ------------------------------ FIELDS ------------------------------

    private static String[][] options;

// -------------------------- STATIC METHODS --------------------------

    public static String[][] getOptions() {
        return options;
    }

    public static boolean start(RootDoc root) {
        options = root.options();
//        System.out.println("Documenting using Alice with " + root.options().length + " options.");
        return true;
    }

    public static int optionLength(String option) {
//        System.out.println("javadoc asked Alice about the length of the '" + option + "' option.");
        return 1;
    }

    public static boolean validOptions(String options[][], DocErrorReporter reporter) {
        return true;
    }
}
