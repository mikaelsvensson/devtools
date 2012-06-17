package info.mikaelsvensson.doclet.shared;

import com.sun.javadoc.Doc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

public class SourceFileTagHandler extends AbstractTagHandler {

    public static final String TAG_NAME = "embed";

    private enum FileType {
        CLASS {
            @Override
            String getFile(final File sourceFolder, final String fileExpression) throws IOException {
                File sourceFile = new File(sourceFolder, fileExpression.replace('.', File.separatorChar) + ".java");
                return MessageFormat.format("</p><pre class=\"{0}\"><![CDATA[{1}]]></pre><p>", getStyleSheetClassByFileType(sourceFile), getFileContent(sourceFile));
            }
        },
        FILE {
            @Override
            String getFile(final File sourceFolder, final String fileExpression) throws IOException {
                File sourceFile = new File(sourceFolder.getParentFile(), fileExpression.replace('/', File.separatorChar));
                return MessageFormat.format("</p><pre class=\"{0}\"><![CDATA[{1}]]></pre><p>", getStyleSheetClassByFileType(sourceFile), getFileContent(sourceFile));
            }
        },
        INCLUDE {
            @Override
            String getFile(final File sourceFolder, final String fileExpression) throws IOException {
                File sourceFile = new File(sourceFolder.getParentFile(), fileExpression.replace('/', File.separatorChar));
                return getFileContent(sourceFile);
            }
        };

        private static String getStyleSheetClassByFileType(File file) {
            int pos = file.getName().lastIndexOf('.');
            if (pos > -1) {
                String ext = file.getName().toLowerCase().substring(pos + 1);
                return "embedded-" + ext + "-file";
            }
            return "";
        }

        abstract String getFile(final File sourceFolder, String fileExpression) throws IOException;

        private static String getFileContent(final File file) throws IOException {
            if (file.exists() && file.isFile()) {
                FileReader reader = new FileReader(file);
                int size = (int) file.length();
                char[] content = new char[size];
                reader.read(content, 0, size);
                return new String(content);
            } else {
                throw new FileNotFoundException(file.getAbsolutePath());
            }
        }
    }

    public SourceFileTagHandler() {
        super(TAG_NAME);
    }

    @Override
    public String toString(final Tag tag) throws TagHandlerException {
        String text = tag.text();
        try {
            File sourceFolder = null;
            Doc holder = tag.holder();
            if (holder instanceof PackageDoc) {
                sourceFolder = getSourceFolder((PackageDoc) holder, tag.position().file());
            } else if (holder instanceof ProgramElementDoc) {
                sourceFolder = getSourceFolder(((ProgramElementDoc) holder).containingPackage(), tag.position().file());
            } else {
                throw new TagHandlerException("Cannot use source tag in documentation for " + tag.holder().name() + ".");
            }
            int pos = text.indexOf(' ');
            if (pos > -1) {
                String type = text.substring(0, pos).trim();
                String expr = text.substring(pos + 1).trim();
                return FileType.valueOf(type.toUpperCase()).getFile(sourceFolder, expr);
            } else {
                throw new TagHandlerException(TAG_NAME + " must have two parameters, resource type and a resource specifier.");
            }
        } catch (IOException e) {
            throw new TagHandlerException(e);
        } catch (IllegalArgumentException e) {
            throw new TagHandlerException(e);
        }
    }

    private File getSourceFolder(PackageDoc currentClassPackage, final File currentSourceFile) {
        File sourceFolder = currentSourceFile.getParentFile();
        if (!"".equals(currentClassPackage)) {
            int i = -1;
            do {
                sourceFolder = sourceFolder.getParentFile();
            } while ((i = currentClassPackage.name().indexOf('.', i + 1)) != -1);
        }
        return sourceFolder;
    }

}
