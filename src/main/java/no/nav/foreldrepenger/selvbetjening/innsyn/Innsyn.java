package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.innsyn.vedtak.Vedtak;

public interface Innsyn extends Pingable, RetryAware {

    Uttaksplan hentUttaksplan(String saksnummer);

    Uttaksplan hentUttaksplanAnnenPart(String annenPart);

    List<Sak> hentSaker();

    Vedtak hentVedtak(String saksnummer);

}
