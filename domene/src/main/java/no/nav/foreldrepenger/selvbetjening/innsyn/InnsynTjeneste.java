package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.common.innsyn.inntektsmelding.FpOversiktInntektsmeldingDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartSak;
import no.nav.foreldrepenger.common.innsyn.Saker;

@Service
public class InnsynTjeneste implements Innsyn {
    private final InnsynConnection innsynConnection;

    private static final Logger LOG = LoggerFactory.getLogger(InnsynTjeneste.class);

    public InnsynTjeneste(InnsynConnection innsynConnection) {
        this.innsynConnection = innsynConnection;
    }

    @Override
    public Saker hentSaker() {
        LOG.info("Henter saker for pålogget bruker");
        return innsynConnection.hentSaker();
    }

    @Override
    public List<String> hentManglendeVedlegg(Saksnummer saksnr) {
        return innsynConnection.hentManglendeVedlegg(saksnr);
    }

    @Override
    public List<TilbakekrevingsInnslag> hentUttalelserOmTilbakekreving() {
        return innsynConnection.hentUttalelserOmTilbakekreving();
    }

    @Override
    public Optional<AnnenPartSak> annenPartSak(AnnenPartSakIdentifikator annenPartSakIdentifikator) {
        if (annenPartSakIdentifikator == null || annenPartSakIdentifikator.annenPartFødselsnummer() == null || annenPartSakIdentifikator.annenPartFødselsnummer().value().isBlank()) {
            return Optional.empty();
        }
        return innsynConnection.hentAnnenpartsSak(annenPartSakIdentifikator);
    }

    @Override
    public Optional<AnnenPartSak> annenPartVedtak(AnnenPartSakIdentifikator annenPartSakIdentifikator) {
        if (annenPartSakIdentifikator == null || annenPartSakIdentifikator.annenPartFødselsnummer() == null || annenPartSakIdentifikator.annenPartFødselsnummer().value().isBlank()) {
            return Optional.empty();
        }
        return innsynConnection.hentAnnenpartsVedtak(annenPartSakIdentifikator);
    }

    @Override
    public List<FpOversiktInntektsmeldingDto> inntektsmeldinger(Saksnummer saksnummer) {
        return innsynConnection.hentInntekstmeldingFor(saksnummer);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + innsynConnection + "]";
    }
}
