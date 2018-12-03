package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.UttaksPeriode;

public interface Innsyn extends Pingable {

    List<UttaksPeriode> hentUttaksplan(String saksnummer);

    List<Sak> hentSaker();

}
