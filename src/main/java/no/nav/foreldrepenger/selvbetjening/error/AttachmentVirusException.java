package no.nav.foreldrepenger.selvbetjening.error;

import java.net.URI;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

public class AttachmentVirusException extends AttachmentException {

    private final URI uri;

    public AttachmentVirusException(Attachment attachment) {
        super(attachment.filename);
        this.uri = attachment.uri();
    }

    public AttachmentVirusException(Vedlegg vedlegg) {
        super(vedlegg.getUrl().toString());
        this.uri = vedlegg.getUrl();
    }

    public URI getUri() {
        return uri;
    }
}
