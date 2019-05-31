package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;

public interface Historikk extends Pingable {

    List<Melding> hentMeldinger();

}
