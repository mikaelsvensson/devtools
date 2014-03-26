package info.mikaelsvensson.devtools.analysis.shared;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommandLineUtilTest {

    private CommandLineUtil util;

    @Before
    public void setUp() throws Exception {
        util = CommandLineUtil.getInstance();
    }

    @Test
    public void testParseArgs() throws Exception {
        Option d = new Option("d", true, "");
        d.setArgs(Option.UNLIMITED_VALUES);

        Option c = new Option("c", "cc", false, "");

        Option a = new Option("a", true, "");

        CommandLine actual = util.parseArgs(new String[]{"-a", "b", "--c", "-d", "e", "f"}, "Help", CommandLineUtilTest.class, a, c, d);

        assertThat(actual.hasOption('a'), is(true));
        assertThat(actual.getOptionValues('a'), is(new String[]{"b"}));
        assertThat(actual.hasOption('c'), is(true));
        assertThat(actual.hasOption("cc"), is(true));
        assertThat(actual.hasOption('d'), is(true));
        assertThat(actual.hasOption("dd"), is(false));
        assertThat(actual.getOptionValues('d'), is(new String[]{"e", "f"}));
    }

    @Test(expected = CommandLineException.class)
    public void testParseArgs_WrongOptionName() throws Exception {
        Option a = new Option("a", true, "");
        util.parseArgs(new String[]{"-unspecified", "parameter"}, "Help", CommandLineUtilTest.class, a);
    }

    @Test
    public void testGetOptions_HappyPath() throws Exception {
        @CliOptions(opts = {
                @CliOptionConfig(name = "a")
        })
        class TestData {

        }
        List<Option> actual = util.getOptions(new TestData());

        assertThat("Number of options", actual.size(), is(1));
        assertThat(actual.get(0).getOpt(), is("a"));
        assertThat(actual.get(0).getArgs(), is(0));
        assertThat(actual.get(0).getArgName(), is(""));
        assertThat(actual.get(0).getDescription(), is(""));
        assertNull(actual.get(0).getLongOpt());
    }

    @Test
    public void testGetOptions_HappyPath2() throws Exception {
        @CliOptions(opts = {
                @CliOptionConfig(name = "a", numArgs = 2, longName = "aa", argsDescription = "argument", description = "a a a", required = true, separator = ' '),
                @CliOptionConfig(name = "b", numArgs = 3, longName = "bb", argsDescription = "argument", description = "b b b", required = false)
        })
        class TestData {

        }
        List<Option> actual = util.getOptions(new TestData());

        assertThat("Number of options", actual.size(), is(2));

        assertThat(actual.get(0).getOpt(), is("a"));
        assertThat(actual.get(0).getArgs(), is(2));
        assertThat(actual.get(0).getArgName(), is("argument"));
        assertThat(actual.get(0).getDescription(), is("a a a"));
        assertThat(actual.get(0).getLongOpt(), is("aa"));
        assertThat(actual.get(0).isRequired(), is(true));
        assertThat(actual.get(0).getValueSeparator(), is(' '));

        assertThat(actual.get(1).getOpt(), is("b"));
        assertThat(actual.get(1).getArgs(), is(3));
        assertThat(actual.get(1).getArgName(), is("argument"));
        assertThat(actual.get(1).getDescription(), is("b b b"));
        assertThat(actual.get(1).getLongOpt(), is("bb"));
        assertThat(actual.get(1).isRequired(), is(false));
        assertThat(actual.get(1).getValueSeparator(), is('='));
    }

    @Test
    public void testGetCliHelp_HappyPath() throws Exception {
        @CliHelp(text = "Help")
        class TestData {

        }
        String actual = util.getCliHelp(new TestData());
        assertThat(actual, is("Help"));
    }

    @Test
    public void testGetCliHelp_Missing() throws Exception {
        class TestData {

        }
        String actual = util.getCliHelp(new TestData());
        assertNull(actual);
    }

    @Test
    public void testGetCliHelp() throws Exception {

    }
}
