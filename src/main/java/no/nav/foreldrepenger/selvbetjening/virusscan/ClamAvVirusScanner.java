package no.nav.foreldrepenger.selvbetjening.virusscan;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

@Service
public class ClamAvVirusScanner implements VirusScanner {

    private final VirusScanConnection connection;

    public ClamAvVirusScanner(VirusScanConnection connection) {
        this.connection = connection;
    }

    @Override
    public void sjekkVirus(Vedlegg vedlegg) {
        connection.scan(vedlegg.getContent(), vedlegg.getUuid());
    }

    @Override
    public void scan(Attachment vedlegg) {
        connection.scan(vedlegg.bytes, vedlegg.filename);
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
