package no.nav.foreldrepenger.selvbetjening.nedetid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NedetidTjeneste implements Nedetid {
    private static final Logger LOG = LoggerFactory.getLogger(NedetidTjeneste.class);

    @Override
    public void registrer(NedetidInfo info) {
        LOG.info("Registrerer nedetid {}", info);
    }

}
