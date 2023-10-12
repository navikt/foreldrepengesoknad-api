package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.ForeldrepengerMapper.tilLukketPeriodeMedVedlegg;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.ForeldrepengerMapper.tilSøker;

import java.time.LocalDate;

import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Fordeling;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;

final class EndringForeldrepengerMapper {

    private EndringForeldrepengerMapper() {
    }

    static Endringssøknad tilEndringForeldrepengesøknad(EndringssøknadForeldrepengerDto endringssøknadFP) {
        return new Endringssøknad(
            LocalDate.now(),
            tilSøker(endringssøknadFP.søker()),
            tilYtelse(endringssøknadFP),
            null,
            tilVedlegg(endringssøknadFP.vedlegg()),
            endringssøknadFP.saksnummer());
    }

    private static Foreldrepenger tilYtelse(EndringssøknadForeldrepengerDto f) {
        return new Foreldrepenger(
            null,
            null,
            null,
            null,
            null,
            tilFordeling(f),
            null
        );
    }

    private static Fordeling tilFordeling(EndringssøknadForeldrepengerDto f) {
        return new Fordeling(
            f.annenforelder().erInformertOmSøknaden(),
            tilLukketPeriodeMedVedlegg(f.uttaksplan()),
            f.ønskerJustertUttakVedFødsel()
        );
    }
}

