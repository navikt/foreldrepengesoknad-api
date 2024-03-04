package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilRettigheter;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilSøker;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilUttaksplan;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;


public final class EndringForeldrepengerMapper {

    private EndringForeldrepengerMapper() {
    }

    public static Endringssøknad tilEndringForeldrepengesøknad(EndringssøknadForeldrepengerDto endringssøknadFP, LocalDate mottattDato) {
        var vedlegg = endringssøknadFP.vedlegg();
        return new Endringssøknad(
            mottattDato,
            tilSøker(endringssøknadFP.søker()),
            tilYtelse(endringssøknadFP, vedlegg),
            endringssøknadFP.tilleggsopplysninger(),
            tilVedlegg(vedlegg),
            endringssøknadFP.saksnummer());
    }

    private static Foreldrepenger tilYtelse(EndringssøknadForeldrepengerDto f, List<VedleggDto> vedlegg) {
        return new Foreldrepenger(
            tilAnnenForelder(f.annenForelder()),
            tilRelasjonTilBarn(f.barn(), vedlegg),
            tilRettigheter(f.søker(), f.annenForelder()),
            null,
            null,
            tilUttaksplan(f.uttaksplan(), f.annenForelder(), vedlegg),
            null
        );
    }
}

