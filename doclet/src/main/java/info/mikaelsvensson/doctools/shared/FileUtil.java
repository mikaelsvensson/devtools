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

package info.mikaelsvensson.doctools.shared;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class FileUtil {

    public static List<File> findFiles(File folder, Pattern filePattern) {
        System.out.println("Finding files in " + folder.getAbsolutePath() + " using " + filePattern.pattern());
        List<File> files = new ArrayList<File>();
        findFiles(folder, filePattern, files);
        return files;
    }

    private static void findFiles(File folder, Pattern filePattern, List<File> resultList) {
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                findFiles(f, filePattern, resultList);
            } else if (f.isFile()) {
                if (filePattern.matcher(f.getName()).matches()) {
                    resultList.add(f);
                }
            }
        }
    }

    public static interface FileGrouper {
        String getGroupId(File file);
    }
    public static String getFileContent(File file) throws IOException {
        FileReader reader = new FileReader(file);
        char[] content = new char[(int) file.length()];
        reader.read(content, 0, content.length);
        return new String(content);
    }

    public static File getFileWithOtherExtension(File file, String newExtension) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0) {
            lastDot = name.length();
        }
        return new File(file.getParent(), name.substring(0, lastDot) + newExtension);
    }

    public static File getFileWithoutExtension(File file) {
        return getFileWithOtherExtension(file, "");
    }

    public static boolean generateThumbnail(File imageFile, int maxWidth, int maxHeight, File output) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        double ratioWidth = 1.0 * maxWidth / width;
        double ratioHeight = 1.0 * maxHeight / height;

        double resizeFactor = Math.min(ratioWidth, ratioHeight);

        if (resizeFactor < 1) {
            int newHeight = (int) (resizeFactor * height);
            int newWidth = (int) (resizeFactor * width);

            BufferedImage img = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

            img.createGraphics().drawImage(bufferedImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(img, "png", output);
            return true;
        } else {
            return false;
        }
    }

    public static Map<String, List<File>> getFileGroups(File folder, FileFilter fileFilter, FileGrouper grouper) {
        Map<String, List<File>> groups = new TreeMap<String, List<File>>();
        for (File file : folder.listFiles(fileFilter)) {
            String groupId = grouper.getGroupId(file);
            if (!groups.containsKey(groupId)) {
                groups.put(groupId, new LinkedList<File>());
            }
            groups.get(groupId).add(file);
        }
        return groups;
    }
}
