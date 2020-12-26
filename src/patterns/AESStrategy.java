package patterns;

import javax.crypto.Cipher;
import java.util.Base64;

public class AESStrategy implements EncodeStrategy {
    private final String key = "IWantToPassTAP12";

    private Cipher cipher;
    private java.security.Key aesKey;

    public AESStrategy() {
        try {
            this.cipher = Cipher.getInstance("AES");
            this.aesKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "AES");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String decode(String body) {
        byte[] encrypted = Base64.getDecoder().decode(body.getBytes());
        String decrypted = null;

        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            decrypted = new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    @Override
    public String encode(String body) {
        byte[] encrypted = new byte[0];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            encrypted = cipher.doFinal(body.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(encrypted);
    }
}
