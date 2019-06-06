package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.Uttaksplan;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.Vedtak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public interface Innsyn extends Pingable {

    Uttaksplan hentUttaksplan(String saksnummer);

    Uttaksplan hentUttaksplan(Fødselsnummer annenPart);

    List<Sak> hentSaker();

    Vedtak hentVedtak(String saksnummer);

}
