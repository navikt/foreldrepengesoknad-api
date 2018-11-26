package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.util.List;

public interface Innsyn {

    List<UttaksPeriode> hentUttaksplan(String saksnummer);

    List<Sak> hentSaker();

}
