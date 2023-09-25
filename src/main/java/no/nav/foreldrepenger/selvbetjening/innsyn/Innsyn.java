package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.ArkivDokumentDto;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.InntektsmeldingDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto;

public interface Innsyn extends RetryAware {

    Saker hentSaker();

    List<String> hentManglendeVedlegg(Saksnummer saksnr);

    List<TilbakekrevingsInnslag> hentUttalelserOmTilbakekreving();

    Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator request);

    List<TidslinjeHendelseDto> tidslinje(Saksnummer saksnummer);

    List<ArkivDokumentDto> alleDokumenterPåBruker();

    List<ArkivDokumentDto> alleDokumenterPåSak(Saksnummer saksnummer);
    List<InntektsmeldingDto> inntektsmeldinger(Saksnummer saksnummer);
}
