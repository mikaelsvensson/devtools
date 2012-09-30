package info.mikaelsvensson.doctools.sitemapreport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlPageStrategy implements PageStrategy {
    private File file;

    public HtmlPageStrategy(File file) {
        this.file = file;
    }

    @Override
    public String getTitle() {
        try {
            FileInputStream fis = new FileInputStream(file);
            Scanner scanner = new Scanner(file);
//            Pattern pattern = Pattern.compile("\\s+");
            Pattern pattern = Pattern.compile("<title>([^<]+)</title>");
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            Matcher matcher = pattern.matcher(new String(content, Charset.forName("ISO-8859-1")));

            if (matcher.find()) {
                String title = matcher.group(1);
                return title;
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
