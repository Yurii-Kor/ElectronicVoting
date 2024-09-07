package il.ac.sce.elctronicvoting.projectclasses.voter;

import il.ac.sce.elctronicvoting.VotingParameter;
import il.ac.sce.elctronicvoting.projectclasses.encryption.EncryptionAES;
import il.ac.sce.elctronicvoting.projectclasses.encryption.HashingSHA;

import javax.crypto.SecretKey;
import java.io.IOException;

class VoterGenerator {
    private static final String DEFAULT_NAME = "Voter ";

    private final HashingSHA hash;
    private final EncryptionAES encryptor;
    private final SecretKey key;
    private final VoterDB managerDB;

    VoterGenerator () throws IOException {
        this.hash = new HashingSHA();
        this.encryptor = new EncryptionAES();
        this.key = encryptor.loadKeyFromFile();
        this.managerDB = new VoterDB();
    }

    void createVoters() throws Exception {
        if (managerDB.isVotersEmpty()) {
            Voter[] newVoters = generateVoters();
            managerDB.addVotersToDB(newVoters);
        }
    }

    private Voter[] generateVoters() throws Exception {
        int amountVoters = VotingParameter.AMOUNT_VOTERS.getValue();
        int amountCenters = VotingParameter.AMOUNT_CENTERS.getValue();

        Voter[] voters = new Voter[amountVoters];
        for (int i = 0; i < amountVoters; i++) {
            int voterNumber = i + 1;

            String voterId = Integer.toString(VotingParameter.DEFAULT_ID.getValue() + voterNumber);
            String encryptedId = encryptor.encrypt(voterId, key);

            String voterCenter = Integer.toString(voterNumber % amountCenters + 1);
            String hashedCenter = hash.hash(voterCenter);

            String voterName = DEFAULT_NAME + voterNumber;
            String encryptedName = encryptor.encrypt(voterName, key);

            String voterPassword = Integer.toString(voterNumber);
            String hashedPassword = hash.hash(voterPassword);

            voters[i] = new Voter(encryptedId, hashedCenter, encryptedName, hashedPassword);
        }

        return voters;
    }


}
