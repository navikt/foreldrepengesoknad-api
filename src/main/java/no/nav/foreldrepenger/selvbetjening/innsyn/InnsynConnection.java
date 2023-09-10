package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

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

    public List<String> hentManglendeVedlegg(Saksnummer saksnr) {
        return Optional.ofNullable(getForObject(cfg.manglendeOppgaver(saksnr), String[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }

    public List<TilbakekrevingsInnslag> hentUttalelserOmTilbakekreving() {
        return Optional.ofNullable(getForObject(cfg.uttalelseOmTilbakekrevinger(), TilbakekrevingsInnslag[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }
}
