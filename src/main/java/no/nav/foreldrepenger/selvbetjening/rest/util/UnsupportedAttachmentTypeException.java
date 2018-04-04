package no.nav.foreldrepenger.selvbetjening.rest.util;

import org.springframework.http.MediaType;

public class UnsupportedAttachmentTypeException extends RuntimeException {

    public UnsupportedAttachmentTypeException(MediaType mediaType) {
        super("Unsupported media type " + mediaType);
    }
}
