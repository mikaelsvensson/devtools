package se.linkon.sabine.docutil.db2;

/*
public class Db2ConverterDocletAction extends XmlDocletAction {
    public static final String FORMAT_DB2 = "db2";
    public static final String DB2_SCHEMA_FILE = "db2schemafile";
    private File db2SchemaFile;

    public Db2ConverterDocletAction(PropertySet propertySet) {
        super(propertySet);
        String db2schemaFilePath = propertySet.getProperty(FORMAT + "." + PROPERTY + "." + DB2_SCHEMA_FILE);
        if (db2schemaFilePath != null) {
            this.db2SchemaFile = new File(db2schemaFilePath);
        }
    }

    public File getDb2SchemaFile() {
        return db2SchemaFile;
    }

    @Override
    public DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws DocumentCreatorException {
        if (format.equalsIgnoreCase(FORMAT_DB2)) {
            try {
                return new Db2MetadataDocumentCreator(this);
            } catch (ParserConfigurationException e) {
                throw new DocumentCreatorException("Could not create formatter for the format '" + format + "'.", e);
            }
        } else {
            return super.createDocumentCreator(parameters);
        }

    }
}
*/
