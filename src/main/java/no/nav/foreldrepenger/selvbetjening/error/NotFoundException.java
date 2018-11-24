package no.nav.foreldrepenger.selvbetjening.error;

import static org.springframework.core.NestedExceptionUtils.getMostSpecificCause;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Throwable cause) {
        this(cause != null ? getMostSpecificCause(cause).getMessage() : null, cause);
    }

    public NotFoundException(String msg) {
        this(msg, null);
    }

    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
