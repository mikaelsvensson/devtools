package se.linkon.sabine.docutil.xml;

import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import se.linkon.sabine.docutil.shared.propertyset.PropertySet;
import se.linkon.sabine.docutil.shared.propertyset.PropertySetException;

public class XmlAndStandardDoclet extends XmlDoclet {

    protected XmlAndStandardDoclet(RootDoc root, XmlDocletOptions options) {
        super(root, options);
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
        try {
            new XmlDoclet(root, new XmlDocletOptions(new PropertySet(root.options()))).generate();
        } catch (PropertySetException e) {
            root.printError(e.getMessage());
        }

        return true;
    }
}
