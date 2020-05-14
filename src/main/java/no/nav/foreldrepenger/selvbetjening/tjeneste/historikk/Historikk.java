package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.Pingable;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public interface Historikk extends Pingable {

    List<HistorikkInnslag> historikk();

    List<HistorikkInnslag> historikkFor(Fødselsnummer fnr);

    List<String> manglendeVedlegg(String saksnr);

    List<String> manglendeVedleggFor(Fødselsnummer fnr, String saksnr);

}
