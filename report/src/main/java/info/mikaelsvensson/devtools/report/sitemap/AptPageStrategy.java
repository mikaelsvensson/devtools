package info.mikaelsvensson.devtools.report.sitemap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AptPageStrategy implements PageStrategy {
    private File file;

    public AptPageStrategy(File file) {
        this.file = file;
    }

    @Override
    public String getTitle() {
        try {
            FileInputStream fis = new FileInputStream(file);
            Scanner scanner = new Scanner(file);
//            Pattern pattern = Pattern.compile("\\s+");
            Pattern pattern = Pattern.compile("\\s+-{3,}\\s*\\r?\\n\\s+(.*?)\\s*\\r?\\n\\s+-{3,}");
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            Matcher matcher = pattern.matcher(new String(content, Charset.forName("ISO-8859-1")));

            if (matcher.find()) {
                return matcher.group(1);
            }

//            if (scanner.hasNext()) {
//                String title = scanner.next();
//                return title;
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return file.getName();
    }
}
