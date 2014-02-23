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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IndexEntry {
    private String uri;
    private String title;
    private Map<String, Integer> wordCount;

    public IndexEntry(final String title, final String uri, final Collection<WordCount> words) {
        this.title = title;
        this.uri = uri;
        this.wordCount = new HashMap<String, Integer>();
        for (WordCount word : words) {
            wordCount.put(word.getWord(), word.getCount());
        }
    }

    public String getTitle() {
        return title;
    }

/*
    public void setTitle(final String title) {
        this.title = title;
    }
*/

    public String getUri() {
        return uri;
    }

/*
    public void setUri(final String uri) {
        this.uri = uri;
    }
*/

    public Map<String, Integer> getWordCount() {
        return wordCount;
    }

/*
    public void setWordCount(final Collection<WordCount> wordCount) {
        this.wordCount = wordCount;
    }
*/
}
