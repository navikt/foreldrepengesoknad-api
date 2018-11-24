package no.nav.foreldrepenger.selvbetjening.felles.error;

import org.springframework.http.MediaType;

public abstract class AttachmentException extends RuntimeException {

    private final MediaType mediaType;

    public AttachmentException(MediaType mediaType) {
        this(mediaType, null);
    }

    public AttachmentException(String msg) {
        this(msg, null, null);
    }

    public AttachmentException(MediaType mediaType, Throwable e) {
        this(null, mediaType, e);
    }

    public AttachmentException(String msg, MediaType mediaType, Throwable e) {
        super(msg, e);
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
