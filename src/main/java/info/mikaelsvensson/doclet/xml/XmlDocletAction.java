package info.mikaelsvensson.doclet.xml;

import info.mikaelsvensson.doclet.shared.DocumentCreator;
import info.mikaelsvensson.doclet.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.doclet.xml.documentcreator.StandardDocumentCreator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

public class XmlDocletAction {
    public enum Format {
        STANDARD {
            @Override
            DocumentCreator createDocumentCreator() throws ParserConfigurationException {
                return new StandardDocumentCreator();
            }
        },
        ELEMENTS_ONLY {
            @Override
            DocumentCreator createDocumentCreator() throws ParserConfigurationException {
                return new ElementsOnlyDocumentCreator();
            }
        };

        abstract DocumentCreator createDocumentCreator() throws ParserConfigurationException;
        
        public String simpleName() {
            return name().toLowerCase().replace("_", "");
        }

        public static Format valueOfSimple(String name) {
            for (Format format : values()) {
                if (format.simpleName().equals(name)) {
                    return format;
                }
            }
            return null;
        }
    }

//    private Format format;
    private File output;
    private File transformer;
    private DocumentCreator documentCreator;

    public DocumentCreator getDocumentCreator() {
        return documentCreator;
    }

    public void setDocumentCreator(DocumentCreator documentCreator) {
        this.documentCreator = documentCreator;
    }

/*
    public Format getFormat() {
        return format;
    }
*/

/*
    public void setFormat(Format format) {
        this.format = format;
    }
*/

    public File getOutput() {
        return output;
    }

    public void setOutput(final File output) {
        this.output = output;
    }

    public File getTransformer() {
        return transformer;
    }

    public void setTransformer(final File transformer) {
        this.transformer = transformer;
    }
}
