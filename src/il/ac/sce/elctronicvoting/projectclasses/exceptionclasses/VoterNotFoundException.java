package il.ac.sce.elctronicvoting.projectclasses.exceptionclasses;

public class VoterNotFoundException extends Exception {
    private static final String MESSAGE_HEAD = "Voter with id: ";
    private static final String MESSAGE_FOOTER = " wasn't found.";
    public VoterNotFoundException(String voterId) {
        super(MESSAGE_HEAD + voterId + MESSAGE_FOOTER);
    }
}
