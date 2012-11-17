package info.mikaelsvensson.doctools.report;

import java.io.IOException;

public interface HtmlFileCreatorFactory {
    HtmlFileCreator createNewHtmlPage(String fileName, String documentTitle) throws IOException;
}
