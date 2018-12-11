package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.UttaksplanPeriode;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;

import java.util.List;

public interface Innsyn extends Pingable {

    List<UttaksplanPeriode> hentUttaksplan(String saksnummer);

    List<Sak> hentSaker();

}
