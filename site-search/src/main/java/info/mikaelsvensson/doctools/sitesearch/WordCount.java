package info.mikaelsvensson.doctools.sitesearch;

public class WordCount {
    private String word;
    private int count;

    public WordCount(final String word, final int count) {
        this.word = word;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getWord() {
        return word;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordCount wordCount = (WordCount) o;

        if (!word.equals(wordCount.word)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    public void increaseCount() {
        count++;
    }
}
