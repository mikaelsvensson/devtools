package info.mikaelsvensson.doclet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for simple XmlDoclet. */
public class XmlDocletTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName
     *         name of the test case
     */
    public XmlDocletTest(String testName) {
        super(testName);
    }

    /** @return the suite of tests being tested */
    public static Test suite() {
        return new TestSuite(XmlDocletTest.class);
    }

    /** Rigourous Test :-) */
    public void testApp() {
        assertTrue(true);
    }
}
