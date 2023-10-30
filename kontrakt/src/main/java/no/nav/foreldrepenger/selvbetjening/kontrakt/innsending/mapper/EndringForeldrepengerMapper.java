package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilLukketPeriodeMedVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilRettigheter;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilSøker;

import java.time.LocalDate;

import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Fordeling;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;

final class EndringForeldrepengerMapper {

    private EndringForeldrepengerMapper() {
    }

    static Endringssøknad tilEndringForeldrepengesøknad(EndringssøknadForeldrepengerDto endringssøknadFP, LocalDate mottattDato) {
        return new Endringssøknad(
            mottattDato,
            tilSøker(endringssøknadFP.søker()),
            tilYtelse(endringssøknadFP),
            endringssøknadFP.tilleggsopplysninger(),
            CommonMapper.tilVedlegg(endringssøknadFP.vedlegg()),
            endringssøknadFP.saksnummer());
    }

    private static Foreldrepenger tilYtelse(EndringssøknadForeldrepengerDto f) {
        return new Foreldrepenger(
            tilAnnenForelder(f.annenForelder()),
            tilRelasjonTilBarn(f.barn(), f.situasjon()),
            tilRettigheter(f.søker(), f.annenForelder()),
            null,
            null,
            tilFordeling(f),
            null
        );
    }

    private static Fordeling tilFordeling(EndringssøknadForeldrepengerDto f) {
        return new Fordeling(
            f.annenForelder().erInformertOmSøknaden(),
            tilLukketPeriodeMedVedlegg(f.uttaksplan()),
            f.ønskerJustertUttakVedFødsel()
        );
    }
}

