package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Krypto {

    private final SecretKey key;
    private final String iv;

    private static final String ALGO = "AES/GCM/NoPadding";

    public Krypto(String passphrase, String fnr) {
        if (isEmpty(passphrase) || isEmpty(fnr)) {
            throw new IllegalArgumentException("Both passphrase and fnr must be provided");
        }
        key = key(passphrase, fnr);
        iv = fnr;
    }

    public String encrypt(String plainText) {
        try {
            return Base64.getEncoder().encodeToString(cipher(ENCRYPT_MODE).doFinal(plainText.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException("Error while encrypting text", ex);
        }
    }

    public String decrypt(String encrypted) {
        try {
            return new String(cipher(DECRYPT_MODE).doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception ex) {
            throw new RuntimeException("Error while decrypting text", ex);
        }
    }

    private Cipher cipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(mode, key, new GCMParameterSpec(128, iv.getBytes()));
        return cipher;
    }

    private static SecretKey key(String passphrase, String salt) {
        try {
            return new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), 10000, 256)).getEncoded(),
                    "AES");
        } catch (Exception ex) {
            throw new RuntimeException("Error while generating key", ex);
        }
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
