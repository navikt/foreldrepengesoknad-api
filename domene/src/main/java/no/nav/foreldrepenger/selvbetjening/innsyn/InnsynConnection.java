package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartSak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.common.innsyn.inntektsmelding.FpOversiktInntektsmeldingDto;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentInfoId;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.JournalpostId;
import no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

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

    public boolean trengerDokumentereMorsArbeid(MorArbeidRequestDto morArbeidRequestDto) {
        return postForObject(cfg.trengerDokumentereMorsArbeid(), morArbeidRequestDto, boolean.class);
    }

    public List<TidslinjeHendelseDto> tidslinje(Saksnummer saksnummer) {
        return Optional.ofNullable(getForObject(cfg.tidlinje(saksnummer), TidslinjeHendelseDto[].class))
                .map(Arrays::asList)
                .orElse(emptyList());
    }

    public List<DokumentDto> dokumenter(Saksnummer saksnummer) {
        return Optional.ofNullable(getForObject(cfg.dokumenter(saksnummer), DokumentDto[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }

    public byte[] hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return getForObject(cfg.hentDokument(journalpostId, dokumentId), byte[].class);
    }

    public boolean erOppdatert() {
        return getForObject(cfg.erOppdatert(), boolean.class);
    }
}
