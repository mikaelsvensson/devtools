package info.mikaelsvensson.docutil.db2;

import info.mikaelsvensson.docutil.shared.DocumentCreator;
import info.mikaelsvensson.docutil.xml.XmlDocletAction;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.Map;

public class Db2ConverterDocletAction extends XmlDocletAction {
    private File db2SchemaFile;

    public File getDb2SchemaFile() {
        return db2SchemaFile;
    }

    public void setDb2SchemaFile(File db2SchemaFile) {
        this.db2SchemaFile = db2SchemaFile;
    }
    @Override
    public DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException {
        if (getFormat().equals(Db2ConverterDocletOptions.DOCUMENT_CREATOR_NAME)) {
            try {
                return new Db2MetadataDocumentCreator(this);
            } catch (ParserConfigurationException e) {
                return null;
            }
        } else {
            return super.createDocumentCreator(parameters);
        }

    }
}
