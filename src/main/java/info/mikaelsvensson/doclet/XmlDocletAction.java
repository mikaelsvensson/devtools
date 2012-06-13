package info.mikaelsvensson.doclet;

import java.io.File;

public class XmlDocletAction {
    private File output;
    private File transformer;

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
