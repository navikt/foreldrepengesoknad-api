package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.springframework.http.MediaType;

public abstract class AttachmentException extends RuntimeException implements UserfacingErrormessage {

    private final MediaType mediaType;

    protected AttachmentException(String msg) {
        this(msg, null);
    }

    protected AttachmentException(String msg, Exception cause) {
        this(msg, null, cause);
    }

    protected AttachmentException(String msg, MediaType mediaType, Throwable e) {
        super(msg, e);
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
