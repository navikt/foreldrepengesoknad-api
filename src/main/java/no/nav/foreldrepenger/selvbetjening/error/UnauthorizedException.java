package no.nav.foreldrepenger.selvbetjening.error;

import java.util.Date;

public class UnauthorizedException extends RuntimeException {

    private final Date expDate;

    public UnauthorizedException(Throwable cause) {
        this(null, null, cause);
    }

    public UnauthorizedException(String msg) {
        this(msg, null, null);
    }

    public UnauthorizedException(Date expDate, Throwable cause) {
        this(null, expDate, cause);
    }

    public UnauthorizedException(String msg, Date expDate) {
        this(msg, expDate, null);
    }

    public UnauthorizedException(String msg, Date expDate, Throwable cause) {
        super(msg, cause);
        this.expDate = expDate;
    }

    public Date getExpDate() {
        return expDate;
    }
}
