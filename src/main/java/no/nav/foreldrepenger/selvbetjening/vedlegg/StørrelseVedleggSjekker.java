package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

@Service
public class StørrelseVedleggSjekker implements VedleggSjekker {

    private static final Logger LOG = LoggerFactory.getLogger(StørrelseVedleggSjekker.class);
    private final DataSize maxTotalSize;
    private final DataSize maxEnkelSize;

    public StørrelseVedleggSjekker(@Value("${vedlegg.maxtotal:32MB}") DataSize maxTotal,
            @Value("${vedlegg.maxenkel:8MB}") DataSize maxEnkel) {
        this.maxTotalSize = maxTotal;
        this.maxEnkelSize = maxEnkel;
    }

    @Override
    public void sjekk(Vedlegg... vedlegg) {
        Arrays.stream(vedlegg).forEach(this::sjekkStørrelse);
        sjekkTotalStørrelse(vedlegg);
    }

    @Override
    public void sjekk(Attachment... vedlegg) {
        Arrays.stream(vedlegg).forEach(this::sjekkStørrelse);
        sjekkTotalStørrelse(vedlegg);

    }

    private void sjekkStørrelse(Vedlegg vedlegg) {
        LOG.info("Sjekker størrelse for {}", vedlegg);
        if ((vedlegg.getContent() != null) && (vedlegg.getContent().length > maxEnkelSize.toBytes())) {
            throw new AttachmentTooLargeException(vedlegg, maxEnkelSize);
        }
    }

    private void sjekkTotalStørrelse(Vedlegg... vedlegg) {
        LOG.info("Sjekker total størrelse for {} vedlegg", vedlegg.length);
        long total = safeStream(vedlegg)
                .filter(v -> v.getContent() != null)
                .mapToLong(v -> v.getContent().length)
                .sum();
        if (total > maxTotalSize.toBytes()) {
            throw new AttachmentsTooLargeException(total, maxTotalSize);
        }
    }

    private void sjekkStørrelse(Attachment vedlegg) {
        LOG.info("Sjekker størrelse for {}", vedlegg);
        if (vedlegg.size.toBytes() > maxEnkelSize.toBytes()) {
            throw new AttachmentTooLargeException(vedlegg.size, maxEnkelSize);
        }
    }

    private void sjekkTotalStørrelse(Attachment... vedlegg) {
        LOG.info("Sjekker total størrelse for {} vedlegg", vedlegg.length);
        long total = safeStream(vedlegg)
                .filter(v -> v.bytes != null)
                .mapToLong(v -> v.bytes.length)
                .sum();
        if (total > maxTotalSize.toBytes()) {
            throw new AttachmentsTooLargeException(total, maxTotalSize);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [maxTotalSize=" + maxTotalSize + ", maxEnkelSize=" + maxEnkelSize + "]";
    }

}
