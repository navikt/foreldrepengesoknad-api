package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

public class MellomlagringKrypto {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    private final String passphrase;

    public MellomlagringKrypto(String passphrase) {
        this.passphrase = passphrase;
    }

    public String katalognavn(String plaintext) {
        return hexBinary(encrypt(plaintext, plaintext).getBytes());
    }

    public String encrypt(String plaintext, String fnr) {
        return new Krypto(passphrase, fnr).encrypt(plaintext);
    }

    public String decrypt(String encrypted, String fnr) {
        return new Krypto(passphrase, fnr).decrypt(encrypted);
    }

    public String hexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}
