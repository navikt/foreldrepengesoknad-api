package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.springframework.http.MediaType;

public abstract class AttachmentException extends RuntimeException {

    private final MediaType mediaType;

    public AttachmentException(String msg) {
        this(msg, null);
    }

    public AttachmentException(String msg, Exception cause) {
        this(msg, null, cause);
    }

    public AttachmentException(String msg, MediaType mediaType, Throwable e) {
        super(msg, e);
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
