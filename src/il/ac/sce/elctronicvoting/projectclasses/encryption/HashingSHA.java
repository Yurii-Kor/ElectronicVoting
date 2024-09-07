package il.ac.sce.elctronicvoting.projectclasses.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashingSHA {
    private static final String ALGORITHM = "SHA-256";

    public String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] encodedHash = digest.digest(data.getBytes());
        return Base64.getEncoder().encodeToString(encodedHash);
    }
}
