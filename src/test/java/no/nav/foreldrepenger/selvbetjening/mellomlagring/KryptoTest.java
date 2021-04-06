package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class KryptoTest {

    @Test
    void encryptDecryptAndMatch() {
        String plainText = "crypto ftw!";
        var c = new Krypto("el passo phraso som er fryktelig lang OG kronglete og det er jo en god ting", "12345678910");
        assertEquals(plainText, c.decrypt(c.encrypt(plainText)));
    }

    @Test
    void passphraseMustBeProvided() {
        assertThrows(IllegalArgumentException.class, () -> new Krypto(null, "12345678910"));
    }

    @Test
    void fnrMustBeProvided() {
        assertThrows(IllegalArgumentException.class, () -> new Krypto("tjobing", null));
    }

}
