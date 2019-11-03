package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

public class MellomlagringException extends RuntimeException {

    public MellomlagringException(String msg) {
        this(msg, null);
    }

    public MellomlagringException(Throwable cause) {
        this(null, cause);
    }

    public MellomlagringException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
