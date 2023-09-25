package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.ArkivDokumentDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentInfoId;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.JournalpostId;
import no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto;

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

    public List<TidslinjeHendelseDto> tidslinje(Saksnummer saksnummer) {
        return Optional.ofNullable(getForObject(cfg.tidslinje(saksnummer), TidslinjeHendelseDto[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }

    public List<ArkivDokumentDto> alleDokumenterPåBruker() {
        return Optional.ofNullable(getForObject(cfg.alleDokumenter(), ArkivDokumentDto[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }

    public List<ArkivDokumentDto> alleDokumenterPåSak(Saksnummer saksnummer) {
        return Optional.ofNullable(getForObject(cfg.alleDokumenter(saksnummer), ArkivDokumentDto[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }

    public ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return getForEntity(cfg.hentDokument().toString(), byte[].class, journalpostId.value(), dokumentId.value());
    }
}
