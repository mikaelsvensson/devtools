package info.mikaelsvensson.doclet.xml;

import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;

public class XmlAndStandardDoclet extends XmlDoclet {

    protected XmlAndStandardDoclet(RootDoc root) {
        super(root);
    }

    public static int optionLength(String option) {
        int len = XmlDoclet.optionLength(option);
        if (len > 0) {
            return len;
        } else {
            return Standard.optionLength(option);
        }
    }

    public static boolean validOptions(String[][] strings, com.sun.javadoc.DocErrorReporter docErrorReporter) {
        return Standard.validOptions(strings, docErrorReporter);
    }

    public static boolean start(RootDoc root) {
        root.printNotice("Generating standard API documentation");
        Standard standard = new Standard();
        standard.start(root);

        root.printNotice("Generating XML based API documentation");
        XmlDoclet.start(root);

        return true;
    }
}
