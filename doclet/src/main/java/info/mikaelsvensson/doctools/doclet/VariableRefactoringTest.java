package info.mikaelsvensson.doctools.doclet;

//TODO Remove class
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
