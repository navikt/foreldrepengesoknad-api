package no.nav.foreldrepenger.selvbetjening.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Crypto {

    private final SecretKey key;
    private String iv;

    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final String FILLER = "00000";

    public Crypto(String passphrase, String fnr) {
        if (isEmpty(passphrase) || isEmpty(fnr)) {
            throw new IllegalArgumentException("Both passphrase and fnr must be provided");
        }
        key = key(passphrase, fnr);
        iv = fnr + FILLER; // iv must be 16 bytes
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException("Error while encrypting text", ex);
        }
    }

    public String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception ex) {
            throw new RuntimeException("Error while decrypting text", ex);
        }
    }

    private SecretKey key(String passphrase, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            char[] passwordChars = passphrase.toCharArray();
            KeySpec spec = new PBEKeySpec(passwordChars, salt.getBytes(), 10000, 256);
            SecretKey key = factory.generateSecret(spec);
            return new SecretKeySpec(key.getEncoded(), "AES");
        } catch (Exception ex) {
            throw new RuntimeException("Error while generating key", ex);
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

}
