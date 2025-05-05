package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartSak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.common.innsyn.inntektsmelding.FpOversiktInntektsmeldingDto;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentInfoId;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.JournalpostId;
import no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje.TidslinjeHendelseDto;

import java.util.List;
import java.util.Optional;

public interface Innsyn extends RetryAware {

    Saker hentSaker();

    List<String> hentManglendeVedlegg(Saksnummer saksnr);

    List<TilbakekrevingsInnslag> hentUttalelserOmTilbakekreving();

    Optional<AnnenPartSak> annenPartSak(AnnenPartSakIdentifikator annenPartSakIdentifikator);

    Optional<AnnenPartSak> annenPartVedtak(AnnenPartSakIdentifikator request);

    List<FpOversiktInntektsmeldingDto> inntektsmeldinger(Saksnummer saksnummer);

    boolean trengerDokumentereMorsArbeid(MorArbeidRequestDto morArbeidRequestDto);

    List<TidslinjeHendelseDto> tidslinje(Saksnummer saksnummer);

    List<DokumentDto> dokumenter(Saksnummer saksnummer);

    byte[] hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId);

    boolean erOppdatert();
}
