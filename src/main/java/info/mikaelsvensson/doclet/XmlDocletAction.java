package info.mikaelsvensson.doclet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlDocletAction {
    private File output;
    private File transformer;
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
