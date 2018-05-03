package no.nav.foreldrepenger.selvbetjening.crypto;

import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CryptoTest {

    @Test
    public void encryptDecryptAndMatch() {
        String plainText = "crypto ftw!";
        Crypto crypto = new Crypto("el passo phraso som er fryktelig lang OG kronglete og det er jo en god ting",
                "12345678910");
        String encrypted = crypto.encrypt(plainText);
        String decrypted = crypto.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    public void encryptedDiffersFromPlaintext() {
        String plainText = "crypto ftw!";
        Crypto crypto = new Crypto("denne er litt kortere", "12345678910");
        String encrypted = crypto.encrypt(plainText);
        String decoded = new String(Base64.getDecoder().decode(encrypted));
        assertNotEquals(plainText, decoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passphraseMustBeProvided() {
        new Crypto("", "12345678910");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fnrMustBeProvided() {
        new Crypto("tjobing", "");
    }

}
