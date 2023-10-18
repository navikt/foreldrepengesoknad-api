package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.springframework.http.MediaType;

public class AttachmentTypeUnsupportedException extends AttachmentException {

    public AttachmentTypeUnsupportedException(MediaType mediaType) {
        this("Media type " + mediaType + " er ikke støttet", mediaType, null);
    }

    public AttachmentTypeUnsupportedException(Throwable e) {
        this(null, null, e);
    }

    public AttachmentTypeUnsupportedException(String msg, MediaType mediaType, Throwable e) {
        super(msg, mediaType, e);
    }

}
