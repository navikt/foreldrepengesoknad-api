package no.nav.foreldrepenger.selvbetjening.vedlegg;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentTooLargeException;
import no.nav.foreldrepenger.selvbetjening.error.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.util.StreamUtil;
import no.nav.foreldrepenger.selvbetjening.virusscan.VirusScanner;

@Component
public class VedleggSjekker {

    private static final Logger LOG = LoggerFactory.getLogger(VedleggSjekker.class);

    private final DataSize maxTotalSize;
    private final DataSize maxEnkelSize;
    private final VirusScanner virusScanner;
    private final PDFEncryptionChecker encryptionChecker;

    public VedleggSjekker(@Value("${vedlegg.maxtotal:32MB}") DataSize maxTotal,
            @Value("${vedlegg.maxenkel:8MB}") DataSize maxEnkel, VirusScanner virusScanner,
            PDFEncryptionChecker encryptionChecker) {
        this.maxTotalSize = maxTotal;
        this.maxEnkelSize = maxEnkel;
        this.virusScanner = virusScanner;
        this.encryptionChecker = encryptionChecker;
    }

    public void sjekk(Vedlegg... vedlegg) {
        sjekk(Arrays.asList(vedlegg));
    }

    public void sjekkAttachments(Attachment... vedlegg) {
        sjekkAttachments(Arrays.asList(vedlegg));
    }

    public void sjekkAttachments(List<Attachment> vedlegg) {
        LOG.info("Sjekker {} vedlegg {}", vedlegg.size(), vedlegg);
        sjekkAttachmentEnkeltStørrelser(vedlegg);
        sjekkAttachmentTotalStørrelse(vedlegg);
        sjekkAttachmentVirus(vedlegg);
        sjekkAttachmentKryptert(vedlegg);
        LOG.info("Sjekket {} vedlegg OK", vedlegg.size());
    }

    public void sjekk(List<Vedlegg> vedlegg) {
        LOG.info("Sjekker {} vedlegg {}", vedlegg.size(), vedlegg);
        sjekkTotalStørrelse(vedlegg);
        sjekkEnkeltStørrelser(vedlegg);
        sjekkVirus(vedlegg);
        sjekkKryptert(vedlegg);
        LOG.info("Sjekket {} vedlegg OK", vedlegg.size());
    }

    private void sjekkEnkeltStørrelser(List<Vedlegg> vedlegg) {
        StreamUtil.safeStream(vedlegg)
                .forEach(this::sjekkStørrelse);
    }

    private void sjekkAttachmentEnkeltStørrelser(List<Attachment> vedlegg) {
        StreamUtil.safeStream(vedlegg)
                .forEach(this::sjekkAttachmentEnkeltStørrelse);
    }

    private void sjekkKryptert(List<Vedlegg> vedlegg) {
        LOG.info("Sjekker kryptering for {}", vedlegg);
        vedlegg.stream()
                .forEach(encryptionChecker::checkEncrypted);
    }

    private void sjekkAttachmentKryptert(List<Attachment> vedlegg) {
        LOG.info("Sjekker kryptering for {}", vedlegg);
        vedlegg.stream()
                .forEach(encryptionChecker::checkEncrypted);
    }

    private void sjekkVirus(List<Vedlegg> vedlegg) {
        LOG.info("Sjekker virus for {}", vedlegg);
        StreamUtil.safeStream(vedlegg)
                .forEach(virusScanner::scan);
    }

    private void sjekkAttachmentVirus(List<Attachment> vedlegg) {
        LOG.info("Sjekker virus for {}", vedlegg);
        vedlegg.stream()
                .forEach(virusScanner::scan);
    }

    private void sjekkStørrelse(Vedlegg vedlegg) {
        LOG.info("Sjekker størrelse for {}", vedlegg);
        if ((vedlegg.getContent() != null) && (vedlegg.getContent().length > maxEnkelSize.toBytes())) {
            throw new AttachmentTooLargeException(vedlegg, maxEnkelSize);
        }
    }

    private void sjekkAttachmentEnkeltStørrelse(Attachment vedlegg) {
        LOG.info("Sjekker størrelse for {}", vedlegg);
        if (vedlegg.size.toBytes() > maxEnkelSize.toBytes()) {
            throw new AttachmentTooLargeException(vedlegg.size, maxEnkelSize);
        }
    }

    private void sjekkTotalStørrelse(List<Vedlegg> vedlegg) {
        LOG.info("Sjekker total størrelse for {}", vedlegg);
        long total = vedlegg.stream()
                .filter(v -> v.getContent() != null)
                .mapToLong(v -> v.getContent().length)
                .sum();
        if (total > maxTotalSize.toBytes()) {
            throw new AttachmentsTooLargeException(total, maxTotalSize);
        }
    }

    private void sjekkAttachmentTotalStørrelse(List<Attachment> vedlegg) {
        LOG.info("Sjekker total størrelse for {}", vedlegg);
        long total = vedlegg.stream()
                .filter(v -> v.bytes != null)
                .mapToLong(v -> v.bytes.length)
                .sum();
        if (total > maxTotalSize.toBytes()) {
            throw new AttachmentsTooLargeException(total, maxTotalSize);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[maxTotalSize=" + maxTotalSize + ", maxEnkelSize=" + maxEnkelSize
                + ", virusScanner=" + virusScanner + ", encryptionChecker=" + encryptionChecker + "]";
    }

}
