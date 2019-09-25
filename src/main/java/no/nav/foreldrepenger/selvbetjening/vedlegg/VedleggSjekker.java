package no.nav.foreldrepenger.selvbetjening.vedlegg;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentTooLargeException;
import no.nav.foreldrepenger.selvbetjening.error.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan.VirusScanner;

@Component
public class VedleggSjekker {

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
        sjekkAttachmentEnkeltStørrelser(vedlegg);
        sjekkAttachmentTotalStørrelse(vedlegg);
        sjekkAttachmentVirus(vedlegg);
        sjekkAttachmentKryptert(vedlegg);
    }

    public void sjekk(List<Vedlegg> vedlegg) {
        sjekkTotalStørrelse(vedlegg);
        sjekkEnkeltStørrelser(vedlegg);
        sjekkVirus(vedlegg);
        sjekkKryptert(vedlegg);
    }

    private void sjekkEnkeltStørrelser(List<Vedlegg> vedlegg) {
        vedlegg.stream().forEach(this::sjekkStørrelse);
    }

    private void sjekkAttachmentEnkeltStørrelser(List<Attachment> vedlegg) {
        vedlegg.stream().forEach(this::sjekkAttachmentEnkeltStørrelse);
    }

    private void sjekkKryptert(List<Vedlegg> vedlegg) {
        vedlegg.stream().forEach(encryptionChecker::checkEncrypted);
    }

    private void sjekkAttachmentKryptert(List<Attachment> vedlegg) {
        vedlegg.stream().forEach(encryptionChecker::checkEncrypted);
    }

    private void sjekkVirus(List<Vedlegg> vedlegg) {
        vedlegg.stream().forEach(virusScanner::scan);
    }

    private void sjekkAttachmentVirus(List<Attachment> vedlegg) {
        vedlegg.stream().forEach(virusScanner::scan);
    }

    private void sjekkStørrelse(Vedlegg vedlegg) {
        if ((vedlegg.getContent() != null) && (vedlegg.getContent().length > maxEnkelSize.toBytes())) {
            throw new AttachmentTooLargeException(vedlegg, maxEnkelSize);
        }
    }

    private void sjekkAttachmentEnkeltStørrelse(Attachment vedlegg) {
        if (vedlegg.size > maxEnkelSize.toBytes()) {
            throw new AttachmentTooLargeException(vedlegg.size, maxEnkelSize);
        }
    }

    private void sjekkTotalStørrelse(List<Vedlegg> vedlegg) {
        long total = vedlegg.stream()
                .filter(v -> v.getContent() != null)
                .mapToLong(v -> v.getContent().length)
                .sum();
        if (total > maxTotalSize.toBytes()) {
            throw new AttachmentsTooLargeException(total, maxTotalSize);
        }
    }

    private void sjekkAttachmentTotalStørrelse(List<Attachment> vedlegg) {
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
