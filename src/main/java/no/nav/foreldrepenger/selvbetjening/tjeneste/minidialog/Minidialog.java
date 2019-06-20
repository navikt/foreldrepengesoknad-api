package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;

public interface Minidialog extends Pingable {

    List<MinidialogInnslag> hentMinidialoger();

}
