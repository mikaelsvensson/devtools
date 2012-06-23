/*
 * Copyright (c) 2012, Mikael Svensson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the copyright holder nor the names of the
 *       contributors of this software may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package info.mikaelsvensson.docutil.chain;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChainDoclet {

    private static final String CHAIN_OPTION_NAME = "chain";
    private static final String CHAIN_OPTION_SEPARATOR = ":";

    public static final Pattern CHAIN_LINK_DEFINITION = Pattern.compile(CHAIN_OPTION_NAME + CHAIN_OPTION_SEPARATOR + "([a-z]+)" + CHAIN_OPTION_SEPARATOR + "([a-zA-Z0-9.]+)");
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
                    }
                }
            } else {
                int pos = opt.indexOf(CHAIN_OPTION_SEPARATOR);
                if (pos >= 0) {
                    String id = opt.substring(0, pos);
                    String docletOption = "-" + opt.substring(pos + 1);
                    return docletWrappers.get(id).optionLength(docletOption);
                } else {
                    return 1;
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
}
