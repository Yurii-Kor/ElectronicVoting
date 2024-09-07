package il.ac.sce.elctronicvoting.projectclasses.voter;

import java.security.NoSuchAlgorithmException;

public class VoterService {
    public void createVoters() throws Exception {
        new VoterGenerator().createVoters();
    }

    public Voter authenticateVoter(String enteredId, String enteredPassword) throws Exception {
        return new VoterAuthenticator().voterLogin(enteredId, enteredPassword);
    }

    public boolean authenticateVotingCenter (Voter currentVoter,String receivedCenter) throws NoSuchAlgorithmException {
        return new VoterCenterChecker().authenticateCenter(currentVoter, receivedCenter);
    }

    public String[] getHashedIdArray() throws Exception {
        return new VoterIdHashing().getHashedIdArray();
    }
}
