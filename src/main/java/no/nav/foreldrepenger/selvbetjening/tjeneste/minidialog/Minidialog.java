package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.historikk.Svar;

public interface Minidialog extends Pingable {

    List<MinidialogInnslag> hentMinidialoger();

    boolean besvarDialog(Svar svar);

}
