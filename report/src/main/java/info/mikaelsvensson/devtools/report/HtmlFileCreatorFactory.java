package info.mikaelsvensson.devtools.report;

import java.io.File;
import java.io.IOException;

public interface HtmlFileCreatorFactory {
    HtmlFileCreator createNewHtmlPage(File fileName, String documentTitle) throws IOException;
}
