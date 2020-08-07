package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;

import org.junit.jupiter.api.Test;

public class CryptoTest {

    @Test
    public void encryptDecryptAndMatch() {
        String plainText = "crypto ftw!";
        Krypto crypto = new Krypto("el passo phraso som er fryktelig lang OG kronglete og det er jo en god ting",
                "12345678910");
        String encrypted = crypto.encrypt(plainText);
        String decrypted = crypto.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    public void encryptedDiffersFromPlaintext() {
        String plainText = "crypto ftw!";
        Krypto crypto = new Krypto("denne er litt kortere", "12345678910");
        String encrypted = crypto.encrypt(plainText);
        String decoded = new String(Base64.getDecoder().decode(encrypted));
        assertNotEquals(plainText, decoded);
    }

    @Test
    public void passphraseMustBeProvided() {
        assertThrows(IllegalArgumentException.class, () -> new Krypto("", "12345678910"));
    }

    @Test
    public void fnrMustBeProvided() {
        assertThrows(IllegalArgumentException.class, () -> new Krypto("tjobing", ""));
    }

}
