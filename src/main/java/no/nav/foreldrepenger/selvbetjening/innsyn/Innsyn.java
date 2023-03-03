package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

import java.util.Optional;

public interface Innsyn extends Pingable, RetryAware {

    Saker hentSaker();

    Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator request);
}
