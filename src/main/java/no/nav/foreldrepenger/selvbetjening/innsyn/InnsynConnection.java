package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.util.Collections.emptyList;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;

@Deprecated
@Component
public class InnsynConnection extends AbstractRestConnection {

    private final InnsynConfig cfg;

    public InnsynConnection(RestOperations operations, InnsynConfig cfg) {
        super(operations);
        this.cfg = cfg;
    }

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    @Override
    public URI pingURI() {
        return cfg.pingURI();
    }

    // TODO: Flytt denne integrasjonen til oppslagtjeneste på endepunkt /oppslag/arbeidsforhold
    public List<Arbeidsforhold> hentArbeidsForhold() {
        return Optional
                .ofNullable(getForObject(cfg.arbeidsforholdURI(), Arbeidsforhold[].class, false))
                .map(Arrays::asList)
                .orElse(emptyList());
    }

}
