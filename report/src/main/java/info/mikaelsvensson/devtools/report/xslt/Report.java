package info.mikaelsvensson.devtools.report.xslt;

public class Report {
    private String name;
    private String description;
    private String xmlFile;
    private String outputFile;
    private String xslFile;

    public String getDescription() {
        return description;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public String getXslFile() {
        return xslFile;
    }

    public String getOutputFile() {
        return outputFile;
    }
}
