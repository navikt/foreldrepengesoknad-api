package no.nav.foreldrepenger.selvbetjening.felles.error;

import java.util.Date;

public class ForbiddenException extends RuntimeException {

    private final Date expDate;

    public ForbiddenException(Throwable cause) {
        this(null, null, cause);
    }

    public ForbiddenException(String msg) {
        this(msg, null, null);
    }

    public ForbiddenException(Date expDate, Throwable cause) {
        this(null, expDate, cause);
    }

    public ForbiddenException(String msg, Date expDate) {
        this(msg, expDate, null);
    }

    public ForbiddenException(String msg, Date expDate, Throwable cause) {
        super(msg, cause);
        this.expDate = expDate;
    }

    public Date getExpDate() {
        return expDate;
    }

}
