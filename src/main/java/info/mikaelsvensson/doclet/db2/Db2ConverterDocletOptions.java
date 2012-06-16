package info.mikaelsvensson.doclet.db2;

import info.mikaelsvensson.doclet.shared.DocumentCreator;
import info.mikaelsvensson.doclet.xml.XmlDocletOptions;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

public class Db2ConverterDocletOptions extends XmlDocletOptions{
    public static final String PARAMETER_DB2_SCHEMA_FILE = "-db2schemafile";
    public static final String DOCUMENT_CREATOR_NAME = "db2";
    private File db2SchemaFile;

    public Db2ConverterDocletOptions(String[][] args) {
        super(args);
    }

    public File getDb2SchemaFile() {
        return db2SchemaFile;
    }

    public void setDb2SchemaFile(File db2SchemaFile) {
        this.db2SchemaFile = db2SchemaFile;
    }

    @Override
    protected DocumentCreator createDocumentCreator(String name) {
        if (DOCUMENT_CREATOR_NAME.equals(name)) {
            try {
                return new Db2MetadataDocumentCreator(this);
            } catch (ParserConfigurationException e) {
                return null;
            }
        } else {
            return super.createDocumentCreator(name);
        }
    }

    @Override
    protected void initOption(String[] arg) {
        String argName = arg[0];
        if (PARAMETER_DB2_SCHEMA_FILE.equals(argName)) {
            setDb2SchemaFile(new File(arg[1]));
        } else {
            super.initOption(arg);
        }
    }

    public static int optionLength(String option) {
        if (option.equals(PARAMETER_DB2_SCHEMA_FILE)) {
            return 2;
        } else {
            return XmlDocletOptions.optionLength(option);
        }
    }
}
