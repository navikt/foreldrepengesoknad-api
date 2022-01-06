package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.Saker;
import no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan.Uttaksplan;

public interface Innsyn extends Pingable, RetryAware {

    Uttaksplan hentUttaksplan(String saksnummer);

    Uttaksplan hentUttaksplanAnnenPart(String annenPart);

    Saker hentSakerV2();

    List<Sak> hentSaker();

}
