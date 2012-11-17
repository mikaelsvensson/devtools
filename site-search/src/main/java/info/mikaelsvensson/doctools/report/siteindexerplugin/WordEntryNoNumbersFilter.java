package info.mikaelsvensson.doctools.report.siteindexerplugin;

public class WordEntryNoNumbersFilter extends AbstractWordEntryFilter {
    @Override
    public boolean accept(final WordCount entry) {
        for (char c : entry.getWord().toCharArray()) {
            boolean isNumberCharacter = Character.isDigit(c) || c == '.' || c == ',' || c == '%';
            if (!isNumberCharacter) {
                return false;
            }
        }
        return true;
    }
}
