package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;

public interface Innsyn extends Pingable {

    Uttaksplan hentUttaksplan(String saksnummer);

    List<Sak> hentSaker();

    Vedtak hentVedtak(String saksnummer);

}
