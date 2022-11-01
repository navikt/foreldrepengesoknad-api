package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.common.innsyn.v2.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.v2.Saker;
import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;

public interface Innsyn extends Pingable, RetryAware {

    UttaksplanDto hentUttaksplan(Saksnummer saksnummer);

    UttaksplanDto hentUttaksplanAnnenPart(Fødselsnummer annenPart);

    Saker hentSakerV2();

    List<Sak> hentSaker();

    Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator request);
}
