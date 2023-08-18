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
        return hexBinary(encrypt(tokenUtil.getSubject()).getBytes());
    }

    public String encrypt(String plaintext) {
        return new Krypto(passphrase, tokenUtil.getSubject()).encrypt(plaintext);
    }

    public String decrypt(String encrypted) {
        return new Krypto(passphrase, tokenUtil.getSubject()).decrypt(encrypted);
    }

    public String hexBinary(byte[] data) {
        var r = new StringBuilder(data.length * 2);
        for (var b : data) {
            r.append(HEXCODE[(b >> 4) & 0xF]);
            r.append(HEXCODE[(b & 0xF)]);
        }
        return r.toString();
    }
}
