package no.nav.foreldrepenger.selvbetjening.felles.storage;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import no.nav.foreldrepenger.selvbetjening.felles.crypto.Crypto;

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
