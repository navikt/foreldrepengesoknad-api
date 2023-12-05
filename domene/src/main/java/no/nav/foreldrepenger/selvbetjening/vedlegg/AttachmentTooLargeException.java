package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.lang.String.format;

import org.springframework.context.MessageSource;
import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;


public class AttachmentTooLargeException extends AttachmentException {
    private final DataSize size;
    private final DataSize maxVedleggSize;

    public AttachmentTooLargeException(VedleggDto vedlegg, DataSize max) {
        this(DataSize.ofBytes(vedlegg.getContent().length), max);
    }

    public AttachmentTooLargeException(DataSize size, DataSize maxVedleggSize) {
        super(msg(size, maxVedleggSize));
        this.size = size;
        this.maxVedleggSize = maxVedleggSize;
    }

    private static String msg(DataSize attachmentSize, DataSize max) {
        return format("Vedlegg-størrelse er %s MB, men kan ikke overstige %s MB",
                attachmentSize.toMegabytes(), max.toMegabytes());
    }

    @Override
    public String getUserfacingErrorMessage(MessageSource messageSource) {
        return getMessage(messageSource, "vedlegg.for.stort", size.toMegabytes(), maxVedleggSize.toMegabytes());
    }
}
