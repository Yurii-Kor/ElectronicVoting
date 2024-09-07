package il.ac.sce.elctronicvoting.projectclasses.voter;

import il.ac.sce.elctronicvoting.projectclasses.encryption.EncryptionAES;
import il.ac.sce.elctronicvoting.projectclasses.encryption.HashingSHA;

import javax.crypto.SecretKey;
import java.io.IOException;

class VoterIdHashing {
    private final HashingSHA hash;
    private final EncryptionAES encryptor;
    private final SecretKey key;
    private final VoterDB managerDB;

    VoterIdHashing () throws IOException {
        this.hash = new HashingSHA();
        this.encryptor = new EncryptionAES();
        this.key = encryptor.loadKeyFromFile();
        this.managerDB = new VoterDB();
    }

    String[] getHashedIdArray() throws Exception {
        String[] idList = managerDB.getEncryptedIdArray();
        String[] hashedIdList = new String[idList.length];

        for (int i = 0; i < idList.length; i++) {
            String id = encryptor.decrypt(idList[i], key);
            hashedIdList[i] = hash.hash(id);
        }

        return hashedIdList;
    }
}
