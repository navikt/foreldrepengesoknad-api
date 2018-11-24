package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class StorageCrypto {

    private String encryptionPassphrase;

    public StorageCrypto(String encryptionPassphrase) {
        this.encryptionPassphrase = encryptionPassphrase;
    }

    public String encryptDirectoryName(String plaintext) {
        return printHexBinary(encrypt(plaintext, plaintext).getBytes());
    }

    public String encrypt(String plaintext, String fnr) {
        return new Crypto(encryptionPassphrase, fnr).encrypt(plaintext);
    }

    public String decrypt(String encrypted, String fnr) {
        return new Crypto(encryptionPassphrase, fnr).decrypt(encrypted);
    }
}
