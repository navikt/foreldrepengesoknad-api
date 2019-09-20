package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

@Service
public class ClamAvVirusScanner implements VirusScanner {

    private final VirusScanConnection connection;

    public ClamAvVirusScanner(VirusScanConnection connection) {
        this.connection = connection;
    }

    @Override
    public void scan(Vedlegg vedlegg) {
        connection.scan(vedlegg.getContent(), vedlegg.getBeskrivelse());
    }

    @Override
    public void scan(Attachment vedlegg) {
        connection.scan(vedlegg.bytes, vedlegg.filename);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
