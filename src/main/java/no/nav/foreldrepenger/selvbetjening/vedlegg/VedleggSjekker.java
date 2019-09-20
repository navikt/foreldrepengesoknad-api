package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan.VirusScanner;

@Component
public class VedleggSjekker {

    private final DataSize maxTotalSize;
    private final VirusScanner virusScanner;
    private final PDFEncryptionChecker encryptionChecker;

    public VedleggSjekker(@Value("${vedlegg.maxtotal:32MB}") DataSize max, VirusScanner virusScanner,
            PDFEncryptionChecker encryptionChecker) {
        this.maxTotalSize = max;
        this.virusScanner = virusScanner;
        this.encryptionChecker = encryptionChecker;

    }

    public void sjekk(Vedlegg... vedlegg) {
        sjekk(Arrays.asList(vedlegg));
    }

    public void sjekk(List<Vedlegg> vedlegg) {
        sjekkTotalStørrelse(vedlegg);
        sjekkVirus(vedlegg);
        sjekkKryptert(vedlegg);
    }

    private void sjekkKryptert(List<Vedlegg> vedlegg) {
        vedlegg.stream().forEach(encryptionChecker::checkEncrypted);
    }

    private void sjekkVirus(List<Vedlegg> vedlegg) {
        vedlegg.stream().forEach(virusScanner::scan);
    }

    private void sjekkTotalStørrelse(List<Vedlegg> vedlegg) {
        long total = vedlegg.stream()
                .filter(v -> v.getContent() != null)
                .mapToLong(v -> v.getContent().length)
                .sum();
        if (total > maxTotalSize.toBytes()) {
            throw new AttachmentsTooLargeException(
                    format("Samlet filstørrelse for alle vedlegg er %s, men må være mindre enn %s",
                            DataSize.ofBytes(total),
                            maxTotalSize));
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[max=" + maxTotalSize + "]";
    }
}
