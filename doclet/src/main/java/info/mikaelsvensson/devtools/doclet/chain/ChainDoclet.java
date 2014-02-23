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

package info.mikaelsvensson.devtools.doclet.chain;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import info.mikaelsvensson.devtools.doclet.xml.FormatProperty;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Doclet used to use multple doclets in a single Javadoc invokation. This is useful, for
 * example, when generating Maven sites and it is desired to use multiple doclets to
 * produce a set of "reports" instead of only the usual Javadoc API "reports.
 * <p/>
 * Using this doclet is pretty straight forward. In your {@code pom.xml} you define a
 * sequence of doclets to run, assign an identifier to each of them and add this identifier
 * to all doclet parameters that should be sent to that particular doclet.
 * <p/>
 * Example:
 * <p/>
 * {@embed xml ../../pom.xml //*[local-name() = 'additionalparam']}
 *
 * @doclet
 */
public class ChainDoclet {

    /**
     * The doclet's only configuration option, but it is used for two things:
     * <ol>
     *     <li>to define a custom prefix used to determine which options should be relayed to
     *     the doclet at hand</li>
     *     <li>to define the doclet at hand</li>
     * </ol>
     *
     * The format is simple: {@code prefix:full-doclet-class-name}.
     *
     * Example:
     *
     * {@code firstdocet:full.doclet.class.name}
     */
    @FormatProperty
    private static final String CHAIN_OPTION_NAME = "chain";
    private static final String CHAIN_OPTION_SEPARATOR = ":";

    public static final Pattern CHAIN_LINK_DEFINITION = Pattern.compile(CHAIN_OPTION_NAME + CHAIN_OPTION_SEPARATOR + "([a-z0-9]+)" + CHAIN_OPTION_SEPARATOR + "([a-zA-Z0-9.]+)");
    private static Map<String, DocletInvoker> docletWrappers = new LinkedHashMap<String, DocletInvoker>();

    public static boolean start(RootDoc root) {
        boolean result = true;
        for (Map.Entry<String, DocletInvoker> entry : docletWrappers.entrySet()) {
            String id = entry.getKey();
            DocletInvoker invoker = entry.getValue();

            String[][] options = getDocletOptions(id, invoker, root.options());

            root.printNotice("Invoking " + invoker.getDocletClassName() + " (options identified by prefix '" + id + CHAIN_OPTION_SEPARATOR + "')");
            result = result & invoker.start(new CustomOptionsRootDoc(root, options));
        }
        return result;
    }

    private static String[][] getDocletOptions(final String targetDocletId, final DocletInvoker docletInvoker, final String[][] allOptions) {
        List<String[]> docletOptions = new LinkedList<String[]>();
        for (String[] option : allOptions) {
            String[] clonedOption = Arrays.copyOf(option, option.length);
            boolean ignore = false;
            String optionName = clonedOption[0];
            if (optionName.startsWith("-" + CHAIN_OPTION_NAME + CHAIN_OPTION_SEPARATOR)) {
                // Never include the "Doclet chain definition" option in a Doclet option list.
                ignore = true;
            } else if (optionName.equals("-doclet")) {
                // Change the "-doclet" option to match the name of the actual Doclet, not the ChainDoclet Doclet.
                clonedOption[1] = docletInvoker.getDocletClassName();
            } else if (optionName.startsWith("-" + targetDocletId + CHAIN_OPTION_SEPARATOR)) {
                // Current option is targeted for specified Doclet. Remove the Doclet id (the prefix) from the option name before sending it to the Doclet.
                clonedOption[0] = "-" + optionName.substring(targetDocletId.length() + 2);
            } else {
                // Include the current option in the Doclet option list if it is NOT targeted to a specific Doclet, i.e. if it is a "non-prefixed" one.
                ignore = isOptionTargetedForOtherDoclet(optionName, targetDocletId);
            }
            if (!ignore) {
                docletOptions.add(clonedOption);
            }
        }
        return docletOptions.toArray(new String[][]{});
    }

    private static boolean isOptionTargetedForOtherDoclet(final String optionName, final String targetDocletId) {
        for (String docletId : docletWrappers.keySet()) {
            if (!docletId.equals(targetDocletId) && optionName.startsWith("-" + docletId + CHAIN_OPTION_SEPARATOR)) {
                // Ignore options, i.e. "do not include in options list", specifically targeted to other Doclets.
                return true;
            }
        }
        return false;
    }

    public static int optionLength(String option) {
        if (option.length() > 1) {
            String opt = option.substring(1);
            if (opt.startsWith(CHAIN_OPTION_NAME + CHAIN_OPTION_SEPARATOR)) {
                Matcher m = CHAIN_LINK_DEFINITION.matcher(opt);
                if (m.matches() && m.groupCount() == 2) {
                    String id = m.group(1);
                    String docletClassName = m.group(2);
                    try {
                        docletWrappers.put(id, DocletInvoker.getInstance(docletClassName));
                        return 1;
                    } catch (DocletInvoker.DocletWrapperException e) {
                        // Silently ignore.
                        e.printStackTrace();
                    }
                }
            } else {
                int pos = opt.indexOf(CHAIN_OPTION_SEPARATOR);
                if (pos >= 0) {
                    String id = opt.substring(0, pos);
                    String docletOption = "-" + opt.substring(pos + 1);
                    return docletWrappers.get(id).optionLength(docletOption);
                } else
                {
                    int len = 0;
                    for (DocletInvoker invoker : docletWrappers.values())
                    {
                        len = Math.max(len, invoker.optionLength(option));
                    }
                    return len;
                }
            }
        }
        return 0;
    }

    public static boolean validOptions(String allOptions[][], DocErrorReporter reporter) {
        boolean result = true;
        for (Map.Entry<String, DocletInvoker> entry : docletWrappers.entrySet()) {
            String id = entry.getKey();
            DocletInvoker invoker = entry.getValue();

            String[][] options = getDocletOptions(id, invoker, allOptions);

            result = result & invoker.validOptions(options, reporter);
        }
        return result;
    }

    public static LanguageVersion languageVersion()
    {
        return LanguageVersion.JAVA_1_5;
    }
}
