package info.mikaelsvensson.docutil.xml;

import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocletAction {

    public static final String FORMAT_STANDARD = "standard";
    public static final String FORMAT_ENUM = "enum";
    public static final String FORMAT_ELEMENTS_ONLY = "elementsonly";
    public static final String FORMAT = "format";
    public static final String FORMAT_NAME = "name";
    public static final String OUTPUT = "output";
    public static final String PROPERTY = "property";
    public static final String POSTPROCESSOR = "postprocessor";
    public static final String TRANSFORMER = "transformer";

    public XmlDocletAction(PropertySet propertySet) {
        this.format = propertySet.getProperty(FORMAT + "." + FORMAT_NAME);

        File outputPath = new File(propertySet.getProperty(OUTPUT));
        if (outputPath != null) {
            this.output = outputPath;
        }

        this.parameters = propertySet.getPropertySet(FORMAT + '.' + PROPERTY);

        this.postProcessingParameters = propertySet.getProperties(POSTPROCESSOR + "." + PROPERTY);

        this.postProcessor = propertySet.getProperty(POSTPROCESSOR + "." + FORMAT_NAME);

        String transformerPath = propertySet.getProperty(TRANSFORMER);
        if (null != transformerPath) {
            this.transformer = new File(transformerPath);
        }
    }

/*
    public DocumentCreator createDocumentCreator(final Map<String, String> parameters) throws DocumentCreatorException {
        try {
            if (FORMAT_STANDARD.equalsIgnoreCase(format)) {
                return new StandardDocumentCreator(parameters);
            } else if (FORMAT_ENUM.equalsIgnoreCase(format)) {
                return new EnumDocumentCreator(parameters);
            } else if (FORMAT_ELEMENTS_ONLY.equalsIgnoreCase(format)) {
                return new ElementsOnlyDocumentCreator();
            }
        } catch (ParserConfigurationException e) {
            throw new DocumentCreatorException("Could not create formatter for the format '" + format + "'.", e);
        }
        throw new DocumentCreatorException("Could not find a suitable formatter for the format '" + format + "'.");
    }
*/

    private File output;
    private File transformer;
    protected String format;

    public void setFormat(final String format) {
        this.format = format;
    }

    //TODO: MISV 20120618 Rename to formatParameters.
    private PropertySet parameters = new PropertySet();

    //TODO: MISV 20120618 Rearrange/sort fields and members.
    private String postProcessor;

    public String getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(String postProcessor) {
        this.postProcessor = postProcessor;
    }

    public Map<String, String> getPostProcessingParameters() {
        return postProcessingParameters;
    }

    private Map<String, String> postProcessingParameters = new HashMap<String, String>();

    public PropertySet getParameters() {
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
