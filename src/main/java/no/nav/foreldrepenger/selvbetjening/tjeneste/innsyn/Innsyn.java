package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;

public interface Innsyn extends Pingable {

    List<UttaksPeriode> hentUttaksplan(String saksnummer);

    List<Sak> hentSaker();

}
