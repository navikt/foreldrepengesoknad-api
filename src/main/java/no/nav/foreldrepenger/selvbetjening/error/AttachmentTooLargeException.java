package no.nav.foreldrepenger.selvbetjening.error;

import static java.lang.String.format;

import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;

public class AttachmentTooLargeException extends AttachmentException {

    public AttachmentTooLargeException(Vedlegg vedlegg, DataSize max) {
        this(DataSize.ofBytes(vedlegg.getContent().length), max);
    }

    public AttachmentTooLargeException(DataSize size, DataSize maxVedleggSize) {
        this(msg(size, maxVedleggSize));
    }

    private AttachmentTooLargeException(String msg) {
        super(msg);
    }

    private static String msg(DataSize attachmentSize, DataSize max) {
        return format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                attachmentSize, max);
    }
}
