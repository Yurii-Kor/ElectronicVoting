package il.ac.sce.elctronicvoting.projectclasses.voter;

import il.ac.sce.elctronicvoting.projectclasses.encryption.HashingSHA;

import java.security.NoSuchAlgorithmException;

public class VoterCenterChecker {
    private final HashingSHA hash;

    VoterCenterChecker () {
        this.hash = new HashingSHA();
    }

    boolean authenticateCenter(Voter currentVoter,String receivedCenter) throws NoSuchAlgorithmException {
        boolean isReceivedValueCorrect = hash.hash(receivedCenter).equals(currentVoter.getCenter())
                || receivedCenter.equals(currentVoter.getCenter());

        if(isReceivedValueCorrect) {
            currentVoter.setCenter(receivedCenter);
        }

        return isReceivedValueCorrect;
    }
}
