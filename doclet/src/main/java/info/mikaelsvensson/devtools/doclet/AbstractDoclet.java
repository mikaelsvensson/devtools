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

package info.mikaelsvensson.devtools.doclet;

import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;

/**
 * Image with {@image resources/cloud.png} or without {@image resources/cloud.png A cloud} alternative texts.
 */
public class AbstractDoclet {

/*
    public RootDoc getRoot() {
        return root;
    }
*/

    protected RootDoc root;

    protected AbstractDoclet(RootDoc root) {
        this.root = root;
    }

    protected String readOption(String optionName) {
        return readOption(optionName, root);
    }

    protected static String readOption(String optionName, RootDoc root) {
        String[][] options = root.options();
        for (String[] opt : options) {
            if (opt[0].equals(optionName)) {
                return opt[1];
            }
        }
        return null;
    }

    /**
     * Indicate that this doclet supports the 1.5 language features.
     * <p/>
     * Credits to http://stackoverflow.com/questions/5731619/doclet-get-generics-of-a-list
     *
     * @return JAVA_1_5, indicating that the new features are supported.
     */
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

    protected void printNotice(String message) {
        root.printNotice(message);
    }

    protected void printError(Exception e) {
        root.printError(e.getMessage());
    }

    protected void printWarning(Exception e) {
        root.printWarning(e.getMessage());
    }

}
