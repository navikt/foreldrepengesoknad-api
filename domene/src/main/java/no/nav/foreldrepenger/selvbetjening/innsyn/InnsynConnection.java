package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.common.innsyn.inntektsmelding.FpOversiktInntektsmeldingDto;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartSak;
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

    public Optional<AnnenPartSak> hentAnnenpartsSak(AnnenPartSakIdentifikator annenPartSakIdentifikator) {
        return Optional.ofNullable(postForObject(cfg.annenpartsSak(), annenPartSakIdentifikator, AnnenPartSak.class));
    }

    public Optional<AnnenPartSak> hentAnnenpartsVedtak(AnnenPartSakIdentifikator annenPartSakIdentifikator) {
        return Optional.ofNullable(postForObject(cfg.annenpartsVedtak(), annenPartSakIdentifikator, AnnenPartSak.class));
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

    public List<FpOversiktInntektsmeldingDto> hentInntekstmeldingFor(Saksnummer saksnummer) {
        return Optional.ofNullable(getForObject(cfg.inntektsmelding(saksnummer), FpOversiktInntektsmeldingDto[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }
}
