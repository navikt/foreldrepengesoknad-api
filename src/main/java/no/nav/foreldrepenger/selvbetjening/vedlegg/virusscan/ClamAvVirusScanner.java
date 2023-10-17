package no.nav.foreldrepenger.selvbetjening.vedlegg.virusscan;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class ClamAvVirusScanner implements VedleggSjekker {

    private final VirusScanConnection connection;

    public ClamAvVirusScanner(VirusScanConnection connection) {
        this.connection = connection;
    }

    @Override
    public void sjekk(VedleggDto... vedlegg) {
        safeStream(vedlegg)
                .forEach(v -> connection.scan(v.getContent(), v.getUuid()));
    }

    @Override
    public void sjekk(Attachment... vedlegg) {
        safeStream(vedlegg)
                .forEach(v -> connection.scan(v.bytes, v.uuid));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
