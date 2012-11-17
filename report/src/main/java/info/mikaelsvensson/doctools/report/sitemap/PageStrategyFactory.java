package info.mikaelsvensson.doctools.report.sitemap;

import info.mikaelsvensson.doctools.report.HtmlPageStrategy;

import java.io.File;

public class PageStrategyFactory {
    private static PageStrategyFactory ourInstance = new PageStrategyFactory();

    public static PageStrategyFactory getInstance() {
        return ourInstance;
    }

    private PageStrategyFactory() {
    }

    public PageStrategy createPageStrategy(File file) {
        int dotPos = file.getName().lastIndexOf('.');
        if (dotPos >= 0) {
            String ext = file.getName().substring(dotPos).toLowerCase();
            if (".apt".equals(ext)) {
                return new AptPageStrategy(file);
            } else if (".html".equals(ext)) {
                return new HtmlPageStrategy(file);
            }
        }
        return null;
    }
}
