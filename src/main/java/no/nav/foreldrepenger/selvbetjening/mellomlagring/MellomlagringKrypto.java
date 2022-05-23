package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.common.util.TokenUtil;

@Component
public class MellomlagringKrypto {
    private static final char[] HEXCODE = "0123456789ABCDEF".toCharArray();

    private final String passphrase;
    private final TokenUtil tokenUtil;

    public MellomlagringKrypto(@Value("${storage.passphrase}") String passphrase, TokenUtil tokenUtil) {
        this.passphrase = passphrase;
        this.tokenUtil = tokenUtil;
    }

    public String katalognavn() {
        return hexBinary(encrypt(tokenUtil.autentisertBruker().value()).getBytes());
    }

    public String encrypt(String plaintext) {
        return new Krypto(passphrase, tokenUtil.autentisertBruker().value()).encrypt(plaintext);
    }

    public String decrypt(String encrypted) {
        return new Krypto(passphrase, tokenUtil.autentisertBruker().value()).decrypt(encrypted);
    }

    public String hexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEXCODE[(b >> 4) & 0xF]);
            r.append(HEXCODE[(b & 0xF)]);
        }
        return r.toString();
    }
}
