package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import java.util.Optional;

@Component
public class InnsynConnection extends AbstractRestConnection {

    private static final String IKKE_TILGANG_UMYNDIG = "IKKE_TILGANG_UMYNDIG";
    private final InnsynConfig cfg;

    public InnsynConnection(RestOperations operations, InnsynConfig cfg) {
        super(operations);
        this.cfg = cfg;
    }

    public Saker hentSaker() {
        try {
            return getForObject(cfg.saker(), Saker.class);
        } catch (HttpClientErrorException.Forbidden e) {
            if (e.getMessage().contains(IKKE_TILGANG_UMYNDIG)) {
                throw new UmydigBrukerException();
            }
            throw e;
        }

    }
    public Optional<AnnenPartVedtak> hentAnnenpartsVedtak(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return Optional.ofNullable(postForObject(cfg.annenpartsVedtak(), annenPartVedtakIdentifikator, AnnenPartVedtak.class));
    }
}
