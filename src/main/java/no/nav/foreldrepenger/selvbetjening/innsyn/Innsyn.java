package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

public interface Innsyn extends RetryAware {

    Saker hentSaker();

    List<String> hentManglendeVedlegg(Saksnummer saksnr);

    List<TilbakekrevingsInnslag> hentUttalelserOmTilbakekreving();

    Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator request);
}
