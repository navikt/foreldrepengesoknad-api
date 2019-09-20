package no.nav.foreldrepenger.selvbetjening.error;

import java.net.URI;

public class AttachmentVirusException extends AttachmentException {

    public AttachmentVirusException(URI uri) {
        super(uri.toString());
    }
}
