package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.felles.RetryAware;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;

public interface Innsyn extends Pingable, RetryAware {

    Uttaksplan hentUttaksplan(String saksnummer);

    Uttaksplan hentUttaksplanAnnenPart(String annenPart);

    List<Sak> hentSaker();

    Vedtak hentVedtak(String saksnummer);

}
