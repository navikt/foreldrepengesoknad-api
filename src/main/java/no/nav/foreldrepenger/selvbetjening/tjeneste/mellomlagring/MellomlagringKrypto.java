package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class MellomlagringKrypto {

    private final String passphrase;

    public MellomlagringKrypto(String passphrase) {
        this.passphrase = passphrase;
    }

    public String katalognavn(String plaintext) {
        return printHexBinary(encrypt(plaintext, plaintext).getBytes());
    }

    public String encrypt(String plaintext, String fnr) {
        return new Krypto(passphrase, fnr).encrypt(plaintext);
    }

    public String decrypt(String encrypted, String fnr) {
        return new Krypto(passphrase, fnr).decrypt(encrypted);
    }
}
