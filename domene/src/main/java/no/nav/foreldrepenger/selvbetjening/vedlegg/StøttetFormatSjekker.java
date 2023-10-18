package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

@Service
public class StøttetFormatSjekker implements VedleggSjekker {

    private static final List<MediaType> supportedTypes = List.of(IMAGE_JPEG, IMAGE_PNG, APPLICATION_PDF);

    @Override
    public void sjekk(VedleggDto... vedlegg) {
        safeStream(vedlegg).forEach(v -> check(v.getContent()));
    }

    @Override
    public void sjekk(Attachment... vedlegg) {
        safeStream(vedlegg).forEach(v -> check(v.getBytes()));
    }

    private static void check(byte[] content) {
        var detectedType = mediaType(content);
        if (detectedType != null && !supportedTypes.contains(detectedType)) {
            throw new AttachmentTypeUnsupportedException(detectedType);
        };
    }
}
