package il.ac.sce.elctronicvoting.projectclasses.exceptionclasses;

public class IncorrectPasswordException extends Exception {
    private static final String MESSAGE = "Incorrect password entered.";
    public IncorrectPasswordException() {
        super(MESSAGE);
    }
}
