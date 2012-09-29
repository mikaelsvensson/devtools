package info.mikaelsvensson.doctools.sitemapreport;

import java.io.File;

public class PageStrategyFactory {
    private static PageStrategyFactory ourInstance = new PageStrategyFactory();

    public static PageStrategyFactory getInstance() {
        return ourInstance;
    }

    private PageStrategyFactory() {
    }

    public PageStrategy createPageStrategy(File file) {
        String ext = file.getName().substring(file.getName().lastIndexOf('.')).toLowerCase();
        if (".apt".equals(ext)) {
            return new AptPageStrategy(file);
        } else {
            return null;
        }
    }
}
