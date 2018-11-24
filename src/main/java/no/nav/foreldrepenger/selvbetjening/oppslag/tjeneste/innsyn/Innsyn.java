package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.oppslag.json.Sak;

public interface Innsyn {

    List<UttaksPeriode> hentUttaksplan(String saksnummer);

    List<Sak> hentSaker();

}
