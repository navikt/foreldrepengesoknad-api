package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentVirusException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

@Service
public class ClamAvVirusScanner implements VirusScanner {

    private final VirusScanConnection connection;

    public ClamAvVirusScanner(VirusScanConnection connection) {
        this.connection = connection;
    }

    @Override
    public void scan(Vedlegg vedlegg) {
        if (connection.scan(vedlegg)) {
            throw new AttachmentVirusException(vedlegg);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
