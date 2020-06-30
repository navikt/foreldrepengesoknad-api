package no.nav.foreldrepenger.selvbetjening.error;

import static java.lang.String.format;

import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

public class AttachmentTooLargeException extends AttachmentException {

    public AttachmentTooLargeException(Vedlegg vedlegg, DataSize max) {
        this(vedlegg.getContent().length, max);
    }

    public AttachmentTooLargeException(long size, DataSize maxVedleggSize) {
        this(msg(DataSize.ofBytes(size), maxVedleggSize));
    }

    private AttachmentTooLargeException(String msg) {
        super(msg);
    }

    private static String msg(DataSize attachmentSize, DataSize max) {
        return format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                attachmentSize, max);
    }
}
