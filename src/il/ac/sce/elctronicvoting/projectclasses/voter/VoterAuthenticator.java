package il.ac.sce.elctronicvoting.projectclasses.voter;

import il.ac.sce.elctronicvoting.projectclasses.encryption.EncryptionAES;
import il.ac.sce.elctronicvoting.projectclasses.encryption.HashingSHA;
import il.ac.sce.elctronicvoting.projectclasses.exceptionclasses.IncorrectPasswordException;
import il.ac.sce.elctronicvoting.projectclasses.exceptionclasses.VoterNotFoundException;

import javax.crypto.SecretKey;
import java.io.IOException;

class VoterAuthenticator {
    private final HashingSHA hash;
    private final EncryptionAES encryptor;
    private final SecretKey key;
    private final VoterDB managerDB;

    VoterAuthenticator() throws IOException {
        this.hash = new HashingSHA();
        this.encryptor = new EncryptionAES();
        this.key = encryptor.loadKeyFromFile();
        this.managerDB = new VoterDB();
    }

    Voter voterLogin(String enteredId, String enteredPassword) throws Exception {
        String encryptedId = encryptor.encrypt(enteredId, key);
        String hashedPassword = hash.hash(enteredPassword);

        Voter currentVoter = managerDB.getVoterFromDB(encryptedId);

        if (currentVoter == null) {
            throw new VoterNotFoundException(enteredId);
        }

        if(!hashedPassword.equals(currentVoter.getPassword())) {
            throw new IncorrectPasswordException();
        }

        decryptVoter(currentVoter, enteredPassword);
        return currentVoter;
    }

    private void decryptVoter(Voter encryptedVoter, String enteredPassword) throws Exception {
        encryptedVoter.setId(encryptor.decrypt(encryptedVoter.getId(), key));
        encryptedVoter.setName(encryptor.decrypt(encryptedVoter.getName(), key));
        encryptedVoter.setPassword(enteredPassword);
    }
}
