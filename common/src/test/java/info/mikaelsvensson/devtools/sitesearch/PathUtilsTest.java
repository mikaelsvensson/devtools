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

import info.mikaelsvensson.devtools.common.PathUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

public class PathUtilsTest {

    @Test
    @Ignore
    public void testGetRelativePath() throws Exception {
        String[] CHAIN_DOCLETINVOKER_JAVA = /*                      */ {"D:", "Dokument", "Utveckling", "devtools", "doclet", "src", "main", "java", "info", "mikaelsvensson", "devtools", "doclet", "chain", "DocletInvoker.java"};
        String[] CHAIN_DOCROOTWRAPPER_JAVA = /*                     */ {"D:", "Dokument", "Utveckling", "devtools", "doclet", "src", "main", "java", "info", "mikaelsvensson", "devtools", "doclet", "chain", "DocRootWrapper.java"};
        String[] XML_DOCUMENTCREATOR_ENUMDOCUMENTCREATOR_JAVA = /*  */ {"D:", "Dokument", "Utveckling", "devtools", "doclet", "src", "main", "java", "info", "mikaelsvensson", "devtools", "doclet", "xml", "documentcreator", "EnumDocumentCreator.java"};
        String[] XML_FORMATPROPERTY_JAVA = /*                       */ {"D:", "Dokument", "Utveckling", "devtools", "doclet", "src", "main", "java", "info", "mikaelsvensson", "devtools", "doclet", "xml", "FormatProperty.java"};

        String[][] all = new String[][]{CHAIN_DOCLETINVOKER_JAVA, CHAIN_DOCROOTWRAPPER_JAVA, XML_DOCUMENTCREATOR_ENUMDOCUMENTCREATOR_JAVA, XML_FORMATPROPERTY_JAVA};

        for (int i = 0; i < all.length; i++) {
            String[] a = all[i];
            for (int j = 0; j < all.length; j++) {
                String[] b = all[j];
                if (a != b) {
                    testTwoPaths(a, b);
                }
            }
        }
    }

    public void testTwoPaths(final String[] sourcePath, final String[] targetPath) {

        Collection<String> errors = new LinkedList<String>();

        for (int i = 10; i < targetPath.length; i++) {
            String target = StringUtils.join(targetPath, '\\', 0, i + 1);
            for (int j = 10; j < sourcePath.length; j++) {
                String source = StringUtils.join(sourcePath, '\\', 0, j + 1);
                System.out.println(source);
                System.out.println(target);
                File sourceFile = new File(source);
                String relativePath = PathUtils.getRelativePath(sourceFile, new File(target));

                System.out.println(relativePath);
                File sourceFolder = sourceFile.isDirectory() ? sourceFile : sourceFile.getParentFile();

                File actual = new File(sourceFolder, relativePath.replace(PathUtils.SEP, File.separatorChar));
                File expected = new File(target);
                boolean isEqual = actual.toURI().normalize().equals(expected.toURI().normalize());
                if (!isEqual) {
                    errors.add("The path from " + source + " to " + target + " is NOT " + actual);
                }
            }
        }

        assertTrue(errors.toString(), errors.size() == 0);
    }
}
