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

package info.mikaelsvensson.devtools.common;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class PathUtils {

    public static final char SEP = '/';

    public static String getRelativePath(File source, File target) {
        source = source.isDirectory() ? source : source.getParentFile();

        String sourceFixed = fixPath(source);
        String targetFixed = fixPath(target);

        String[] sourceParts = StringUtils.split(sourceFixed, SEP);
        String[] targetParts = StringUtils.split(targetFixed, SEP);
        int sharedParts = 0;
        for (int i = 0; i < sourceParts.length; i++) {
            String sourcePart = sourceParts[i];
            if (targetParts.length == i) {
                break;
            }
            String targetPart = targetParts[i];
            if (sourcePart.equals(targetPart)) {
                sharedParts = i;
            }
        }

        String toSharedRoot = StringUtils.repeat(".." + SEP, sourceParts.length - sharedParts - 1);
        String fromSharedRoot = StringUtils.join(targetParts, SEP, sharedParts + 1, targetParts.length);
        if (StringUtils.isEmpty(toSharedRoot) && StringUtils.isEmpty(fromSharedRoot)) {
            return "." + SEP;
        } else {
            return toSharedRoot + fromSharedRoot;
        }

    }

    private static String fixPath(final File source) {
        String fixed = source.getAbsolutePath();
        if (File.separatorChar != SEP) {
            fixed = fixed.replace(File.separatorChar, SEP);
        }
        return source.isDirectory() ? trailingSlash(fixed) : fixed;
    }

    public static String trailingSlash(String str) {
        if (str.charAt(str.length() - 1) != SEP) {
            return str + SEP;
        }
        return str;
    }
}
