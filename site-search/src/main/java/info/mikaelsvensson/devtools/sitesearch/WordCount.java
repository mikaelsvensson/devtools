/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.sitesearch;

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
