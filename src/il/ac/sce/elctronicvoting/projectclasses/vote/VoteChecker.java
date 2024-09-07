package il.ac.sce.elctronicvoting.projectclasses.vote;

import il.ac.sce.elctronicvoting.Party;
import il.ac.sce.elctronicvoting.VotingParameter;
import il.ac.sce.elctronicvoting.projectclasses.encryption.EncryptionAES;

import javax.crypto.SecretKey;
import java.io.IOException;

public class VoteChecker {
    private final EncryptionAES encryptor;
    private final SecretKey key;

    VoteChecker() throws IOException {
        this.encryptor = new EncryptionAES();
        this.key = encryptor.loadKeyFromFile();
    }

    boolean isHashedIdCorrect(String currentHashedId, String[] hashedIdList) {
        boolean isCorrect = false;
        for (String hashedId : hashedIdList) {
            isCorrect = isCorrect || currentHashedId.equals(hashedId);
        }

        return isCorrect;
    }

    boolean isEncryptedCenterCorrect(String encryptedCenter) {
        try {
            String decryptedCenter = encryptor.decrypt(encryptedCenter, key);
            int center = Integer.parseInt(decryptedCenter);
            return center >= 1 && center <= VotingParameter.AMOUNT_CENTERS.getValue();
        } catch (Exception e) {
            return false;
        }
    }

    boolean isEncryptedChoiceCorrect(String encryptedChoice) {
        try {
            String choice = encryptor.decrypt(encryptedChoice, key);
            return Party.DEMOCRAT.getName().contains(choice) || Party.REPUBLICAN.getName().contains(choice);
        } catch (Exception e) {
            return false;
        }
    }
}
