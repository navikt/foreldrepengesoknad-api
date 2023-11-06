package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilRettigheter;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilSøker;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilUttaksplan;

import java.time.LocalDate;

import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;


public final class EndringForeldrepengerMapper {

    private EndringForeldrepengerMapper() {
    }

    public static Endringssøknad tilEndringForeldrepengesøknad(EndringssøknadForeldrepengerDto endringssøknadFP, LocalDate mottattDato) {
        return new Endringssøknad(
            mottattDato,
            tilSøker(endringssøknadFP.søker()),
            tilYtelse(endringssøknadFP),
            endringssøknadFP.tilleggsopplysninger(),
            tilVedlegg(endringssøknadFP.vedlegg()),
            endringssøknadFP.saksnummer());
    }

    private static Foreldrepenger tilYtelse(EndringssøknadForeldrepengerDto f) {
        return new Foreldrepenger(
            tilAnnenForelder(f.annenForelder()),
            tilRelasjonTilBarn(f.barn()),
            tilRettigheter(f.søker(), f.annenForelder()),
            null,
            null,
            tilUttaksplan(f.uttaksplan(), f.annenForelder()),
            null
        );
    }
}

