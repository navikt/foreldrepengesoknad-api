package no.nav.foreldrepenger.selvbetjening.nedetid;

import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NedetidTjeneste implements Nedetid {
    private static final Logger LOG = LoggerFactory.getLogger(NedetidTjeneste.class);

    @Override
    public void registrer(NedetidInfo info) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Registrerer nedetid {}", escapeHtml(info));
        }
    }

}
