package no.nav.foreldrepenger.selvbetjening.nedetid;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NedetidTjeneste implements Nedetid {
    private static final Logger LOG = LoggerFactory.getLogger(NedetidTjeneste.class);

    @Override
    public void registrer(NedetidInfo info) {
        LOG.info("Registrerer nedetid {}", escapeHtml4(info.toString()));
    }

}
