package se.linkon.sabine.docutil.db2;

import se.linkon.sabine.docutil.shared.propertyset.PropertySet;
import se.linkon.sabine.docutil.shared.propertyset.PropertySetException;
import se.linkon.sabine.docutil.xml.XmlDocletOptions;

public class Db2ConverterDocletOptions extends XmlDocletOptions<Db2ConverterDocletAction> {

    public Db2ConverterDocletOptions(PropertySet propertySet) throws PropertySetException {
        super(propertySet);
    }

    @Override
    protected Db2ConverterDocletAction createAction(PropertySet propertySet) {
        return new Db2ConverterDocletAction(propertySet);
    }

    public static int optionLength(String option) {
        if (option.endsWith(Db2ConverterDocletAction.DB2_SCHEMA_FILE)) {
            return 2;
        } else {
            return XmlDocletOptions.optionLength(option);
        }
    }
}
