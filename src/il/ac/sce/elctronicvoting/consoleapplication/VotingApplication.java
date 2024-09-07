package il.ac.sce.elctronicvoting.consoleapplication;

import il.ac.sce.elctronicvoting.ResultHandler;
import il.ac.sce.elctronicvoting.projectclasses.exceptionclasses.IncorrectPasswordException;
import il.ac.sce.elctronicvoting.projectclasses.exceptionclasses.VoterNotFoundException;
import il.ac.sce.elctronicvoting.projectclasses.voter.Voter;
import il.ac.sce.elctronicvoting.projectclasses.voter.VoterService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class VotingApplication {
    private static final String EMPTY_STRING = "";
    private static final String HEAD = "\nPlease, enter the account details or empty string to exit from application";
    private static final String ID = "ID : ";
    private static final String PASSWORD = "Password : ";

    private final BufferedReader reader;
    private final VoterService service;

    public VotingApplication() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.service = new VoterService();
    }

    public void startVoting() throws Exception {
        service.createVoters();

        while (true) {
            System.out.println(HEAD);

            System.out.print(ID);
            String enteredId = reader.readLine();
            if (enteredId.equals(EMPTY_STRING)) {
                reader.close();
                break;
            }

            System.out.print(PASSWORD);
            String enteredPassword = reader.readLine();
            if (enteredPassword.equals(EMPTY_STRING)) {
                reader.close();
                break;
            }

            Account currentAccount = getAccount(enteredId, enteredPassword, reader);
            if (currentAccount != null) {
                currentAccount.goToAccount();
            }
        }
    }

    private Account getAccount(String id, String password, BufferedReader reader) throws Exception {
        Account currentAccount = getResultHandlerAccount(id, password, reader);
        if (currentAccount != null) {
            return currentAccount;
        }

        currentAccount = getVoterAccount(id, password, reader);

        return currentAccount;
    }

    private Account getResultHandlerAccount(String id, String password, BufferedReader reader) {
        if (id.equals(ResultHandler.ID.getValue()) && password.equals(ResultHandler.PASSWORD.getValue())) {
            return new ResultHandlerAccount(reader);
        } else {
            return null;
        }
    }

    private Account getVoterAccount(String id, String password, BufferedReader reader) throws Exception {
        Voter currentVoter = null;
        try {
            currentVoter = service.authenticateVoter(id, password);
        } catch (VoterNotFoundException | IncorrectPasswordException e) {
            System.out.println(e.getMessage());
        }

        if (currentVoter == null) {
            return null;
        } else {
            return new VoterAccount(currentVoter, reader);
        }
    }
}
