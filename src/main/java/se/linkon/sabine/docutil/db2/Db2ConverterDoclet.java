package se.linkon.sabine.docutil.db2;

import com.sun.javadoc.RootDoc;
import se.linkon.sabine.docutil.shared.propertyset.PropertySet;
import se.linkon.sabine.docutil.shared.propertyset.PropertySetException;
import se.linkon.sabine.docutil.xml.XmlDoclet;

public class Db2ConverterDoclet extends XmlDoclet<Db2ConverterDocletAction, Db2ConverterDocletOptions> {
    protected Db2ConverterDoclet(RootDoc root, Db2ConverterDocletOptions options) {
        super(root, options);
    }
    public static boolean start(RootDoc root) throws PropertySetException {
        return new Db2ConverterDoclet(root, new Db2ConverterDocletOptions(new PropertySet(root.options()))).generate();
    }
    public static int optionLength(String option) {
        return Db2ConverterDocletOptions.optionLength(option);
    }
}
