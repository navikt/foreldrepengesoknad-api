package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;

import org.junit.jupiter.api.Test;

class CryptoTest {

    @Test
    void encryptDecryptAndMatch() {
        var plainText = "crypto ftw!";
        var crypto = new Krypto("el passo phraso som er fryktelig lang OG kronglete og det er jo en god ting",
                "12345678910");
        var encrypted = crypto.encrypt(plainText);
        var decrypted = crypto.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    void encryptedDiffersFromPlaintext() {
        var plainText = "crypto ftw!";
        var crypto = new Krypto("denne er litt kortere", "12345678910");
        var encrypted = crypto.encrypt(plainText);
        var decoded = new String(Base64.getDecoder().decode(encrypted));
        assertNotEquals(plainText, decoded);
    }

    @Test
    void passphraseMustBeProvided() {
        assertThrows(IllegalArgumentException.class, () -> new Krypto("", "12345678910"));
    }

    @Test
    void fnrMustBeProvided() {
        assertThrows(IllegalArgumentException.class, () -> new Krypto("tjobing", ""));
    }

}
