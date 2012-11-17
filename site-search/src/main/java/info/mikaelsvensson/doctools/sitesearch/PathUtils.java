package info.mikaelsvensson.doctools.sitesearch;

import org.apache.commons.lang.StringUtils;

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
