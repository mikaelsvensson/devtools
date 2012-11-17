package info.mikaelsvensson.doctools.report.sitesearch;

import java.util.*;

public class WordEntryBlackListFilter extends AbstractWordEntryFilter {

    Set<String> blackList = new HashSet<String>();

    public WordEntryBlackListFilter(final String[] localeNames) {
        for (String localeName : localeNames) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("CommonWords", new Locale(localeName));
                if (bundle != null) {
                    String words = bundle.getString("words");
                    StringTokenizer tokenizer = new StringTokenizer(words);
                    while (tokenizer.hasMoreTokens()) {
                        blackList.add(tokenizer.nextToken());
                    }
                }
            } catch (MissingResourceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public boolean accept(final WordCount entry) {
        return !blackList.contains(entry.getWord());
    }
}
