package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import java.util.List;

public interface Innsyn {

    List<UttaksPeriode> hentUttaksplan(String saksnummer);

}
