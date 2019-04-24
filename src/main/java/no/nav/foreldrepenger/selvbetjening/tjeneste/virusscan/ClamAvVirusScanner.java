package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

@Service
public class ClamAvVirusScanner implements VirusScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ClamAvVirusScanner.class);
    private final VirusScanConnection connection;

    public ClamAvVirusScanner(VirusScanConnection connection) {
        this.connection = connection;
    }

    @Override
    public boolean scan(Attachment attachment) {
        if (connection.isEnabled()) {
            return connection.scan(attachment);
        }
        LOG.info("Virus scanning is disabled");
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }
}
