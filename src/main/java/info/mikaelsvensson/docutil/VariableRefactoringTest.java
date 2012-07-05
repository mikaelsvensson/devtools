package info.mikaelsvensson.docutil;

/**
 * Created with IntelliJ IDEA.
 * User: mikael
 * Date: 7/5/12
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableRefactoringTest {
    private int number1, numberA;
    public void cloneText(int text) {
        text = text + text;
        this.text = "update";
        System.out.println("text");
    }
    private String text;

    public Double bigNumber;
}
