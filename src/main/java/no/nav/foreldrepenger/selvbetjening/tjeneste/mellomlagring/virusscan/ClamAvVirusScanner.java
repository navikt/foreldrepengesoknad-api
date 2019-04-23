package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.virusscan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

@Service
public class ClamAvVirusScanner implements VirusScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ClamAvVirusScanner.class);

    @Override
    public boolean scan(Attachment attachment) {
        LOG.info("Scanner (snart) {}", attachment.uuid);
        return true;
    }
}
