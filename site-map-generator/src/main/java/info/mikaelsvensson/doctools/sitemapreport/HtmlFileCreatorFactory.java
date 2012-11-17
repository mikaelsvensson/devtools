package info.mikaelsvensson.doctools.sitemapreport;

import java.io.IOException;

public interface HtmlFileCreatorFactory {
    HtmlFileCreator createNewHtmlPage(String fileName, String documentTitle) throws IOException;
}
