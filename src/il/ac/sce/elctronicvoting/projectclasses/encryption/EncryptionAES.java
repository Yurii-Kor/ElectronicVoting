package il.ac.sce.elctronicvoting.projectclasses.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class EncryptionAES {
    private static final int CHOICE_LENGTH = 3;
    private static final String ALGORITHM = "AES";
    private static final String KEY_DIRECTORY = "secretkey";
    private static final String KEY_FILE = "secretKey.key";

    public void generateAndSaveKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        File keyDir = new File(KEY_DIRECTORY);

        byte[] keyBytes = secretKey.getEncoded();
        try (FileOutputStream fos = new FileOutputStream(new File(keyDir, KEY_FILE))) {
            fos.write(keyBytes);
        }
    }

    public SecretKey loadKeyFromFile() throws IOException {
        File keyFile = new File(KEY_DIRECTORY, KEY_FILE);
        byte[] keyBytes = new byte[(int) keyFile.length()];
        try (FileInputStream fis = new FileInputStream(keyFile)) {
            fis.read(keyBytes);
        }

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
    public int getChoiceLength() {
        return CHOICE_LENGTH;
    }
}
