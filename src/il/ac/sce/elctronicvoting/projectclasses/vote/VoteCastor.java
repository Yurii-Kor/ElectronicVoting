package il.ac.sce.elctronicvoting.projectclasses.vote;

import il.ac.sce.elctronicvoting.projectclasses.encryption.EncryptionAES;
import il.ac.sce.elctronicvoting.projectclasses.encryption.HashingSHA;
import il.ac.sce.elctronicvoting.projectclasses.voter.Voter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Random;

public class VoteCastor {
    private final HashingSHA hash;
    private final EncryptionAES encryptor;
    private final SecretKey key;
    private final VoteDB managerDB;
    VoteCastor () throws IOException {
        this.hash = new HashingSHA();
        this.encryptor = new EncryptionAES();
        this.key = encryptor.loadKeyFromFile();
        this.managerDB = new VoteDB();
    }
    void castVote(Voter currentVoter, String choice) throws Exception {
        String hashedId = hash.hash(currentVoter.getId());
        String encryptedCenter = encryptor.encrypt(currentVoter.getCenter(), key);
        choice = getNamePart(choice, encryptor.getChoiceLength());
        String encryptedChoice = encryptor.encrypt(choice, key);

        managerDB.addVoteToDB(new Vote(hashedId, encryptedCenter, encryptedChoice));
    }

    private String getNamePart(String word, int numChars) {
        if (numChars >= word.length()) {
            return word;
        }

        Random random = new Random();
        int startPos = random.nextInt(word.length() - numChars + 1);

        return word.substring(startPos, startPos + numChars);
    }
}
