package no.nav.foreldrepenger.selvbetjening.minidialog;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

public interface Minidialog extends Pingable, RetryAware {

    List<MinidialogInnslag> hentMinidialoger(Fødselsnummer fnr, boolean activeOnly);

    List<MinidialogInnslag> aktive();

    List<MinidialogInnslag> hentAktiveMinidialogSpørsmål(Fødselsnummer fnr);

}
