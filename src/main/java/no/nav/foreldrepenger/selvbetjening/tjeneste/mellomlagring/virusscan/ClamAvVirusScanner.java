package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.virusscan;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

@Service
public class ClamAvVirusScanner implements VirusScanner {

    private final VirusScanConnection connection;

    public ClamAvVirusScanner(VirusScanConnection connection) {
        this.connection = connection;
    }

    @Override
    public boolean scan(Attachment attachment) {
        return connection.scan(attachment);
    }
}
