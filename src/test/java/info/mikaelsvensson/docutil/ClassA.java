package info.mikaelsvensson.docutil;

import java.util.List;
import java.util.Map;

public class ClassA {
    private String field1;
    public String field2;

    public void voidMethod(String... variableArgs) {

    }

    private int intMethod(int[][] table) {
        return 0;
    }

    /**
     * This method handles numbers.
     *
     * @param numberParameter
     *         parameter comment
     * @param <T>
     *         a comment
     * @return something
     */
    protected <T extends Number> T numberMethod(T numberParameter) {
        return null;
    }

    Map<String, List<String>> getGroupedStrings() {
        return null;
    }

}
