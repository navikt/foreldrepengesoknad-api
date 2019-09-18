package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public interface Historikk extends Pingable {

    List<SøknadInnslag> hentHistorikk();

    List<SøknadInnslag> hentHistorikkFor(Fødselsnummer fnr);

    List<MinidialogInnslag> minidialoger();

}
