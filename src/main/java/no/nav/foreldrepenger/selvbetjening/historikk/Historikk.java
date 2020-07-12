package no.nav.foreldrepenger.selvbetjening.historikk;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

public interface Historikk extends Pingable, RetryAware {

    List<HistorikkInnslag> historikk();

    List<HistorikkInnslag> historikkFor(Fødselsnummer fnr);

    List<String> manglendeVedlegg(String saksnr);

    List<String> manglendeVedleggFor(Fødselsnummer fnr, String saksnr);

}
