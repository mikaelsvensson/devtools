package info.mikaelsvensson.doclet.xml;

import info.mikaelsvensson.doclet.shared.DocumentCreator;
import info.mikaelsvensson.doclet.xml.documentcreator.ElementsOnlyDocumentCreator;
import info.mikaelsvensson.doclet.xml.documentcreator.StandardDocumentCreator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocletAction {
    public DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException {
        return Format.valueOfSimple(format).createDocumentCreator(parameters);
    }

    public enum Format {
        STANDARD {
            @Override
            DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException {
                return new StandardDocumentCreator(parameters);
            }
        },
        ELEMENTS_ONLY {
            @Override
            DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException {
                return new ElementsOnlyDocumentCreator();
            }
        };

        abstract DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException;
        
        public String simpleName() {
            return name().toLowerCase().replace("_", "");
        }

        public static Format valueOfSimple(String name) {
            for (Format format : values()) {
                if (format.simpleName().equalsIgnoreCase(name)) {
                    return format;
                }
            }
            return null;
        }
    }

    private File output;
    private File transformer;
//    private Format format;
    private String format;

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

/*
    public DocumentCreator getDocumentCreator() throws ParserConfigurationException {
        Format f = Format.valueOfSimple(format);
        return f.createDocumentCreator(parameters);
    }
*/

    private Map<String, String> parameters = new HashMap<String, String>();

    public Map<String, String> getParameters() {
        return parameters;
    }

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
