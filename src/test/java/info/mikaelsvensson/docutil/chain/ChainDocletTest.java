package info.mikaelsvensson.docutil.chain;

import com.sun.tools.doclets.standard.Standard;
import info.mikaelsvensson.docutil.ClassA;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertArrayEquals;

public class ChainDocletTest {
    @Test
    public void testAliceAndBob() throws Exception {
        performTest(ClassA.class,
                "-chain:alice:" + TestDocletAlice.class.getName(),
                "-chain:bob:" + TestDocletBob.class.getName(),
                "-alice:option-for-alice",
                "-bob:option1-for-bob",
                "value1-for-bob",
                "-bob:option2-for-bob",
                "value2-for-bob",
                "-shared");

        String[][] expectedAliceOptions = {
                {"-doclet", TestDocletAlice.class.getName()},
                {"-option-for-alice"},
                {"-shared"}};
        assertArrayEquals("Options to Alice", expectedAliceOptions, TestDocletAlice.getOptions());

        String[][] expectedBobOptions = {
                {"-doclet", TestDocletBob.class.getName()},
                {"-option1-for-bob", "value1-for-bob"},
                {"-option2-for-bob", "value2-for-bob"},
                {"-shared"}};
        assertArrayEquals("Options to Bob", expectedBobOptions, TestDocletBob.getOptions());
    }

    @Test
    @Ignore
    public void testAliceAndJavadoc() throws Exception {
        performTest(ClassA.class,
                "-chain:alice:" + TestDocletAlice.class.getName(),
                "-chain:standard:" + Standard.class.getName(),
                "-alice:option-for-alice");

        String[][] expectedAliceOptions = {
                {"-doclet", TestDocletAlice.class.getName()},
                {"-option-for-alice"}};
        assertArrayEquals("Option to Alice", TestDocletAlice.getOptions(), expectedAliceOptions);
    }

    private void performTest(Class testClass, String... options) throws IOException, URISyntaxException, SAXException, ParserConfigurationException {

        String testClassFileName = new File(".\\src\\test\\resources\\" + testClass.getName().replace('.', File.separatorChar) + ".java").getAbsolutePath();

        String[] args = new String[2 + options.length + 1];
        args[0] = "-doclet";
        args[1] = ChainDoclet.class.getName();
        for (int i = 0; i < options.length; i++) {
            args[i + 2] = options[i];
        }
        args[args.length - 1] = testClassFileName;

        com.sun.tools.javadoc.Main.execute(
                "javadoc",
                args[1],
                args);
    }
}
