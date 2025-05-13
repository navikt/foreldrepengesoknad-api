package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;

import java.time.LocalDate;
import java.util.List;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilRettigheter;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilSøker;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilUttaksplan;


public final class EndringForeldrepengerMapper {

    private EndringForeldrepengerMapper() {
    }

    public static Endringssøknad tilEndringForeldrepengesøknad(EndringssøknadForeldrepengerDto endringssøknadFP,
                                                               List<VedleggDto> påkrevdeVedlegg,
                                                               LocalDate mottattDato) {
        return new Endringssøknad(mottattDato, tilSøker(endringssøknadFP.rolle(), endringssøknadFP.språkkode()),
                                  tilYtelse(endringssøknadFP, påkrevdeVedlegg), endringssøknadFP.tilleggsopplysninger(),
                                  tilVedlegg(påkrevdeVedlegg),
                                  endringssøknadFP.saksnummer());
    }

    private static Foreldrepenger tilYtelse(EndringssøknadForeldrepengerDto f, List<VedleggDto> vedlegg) {
        return new Foreldrepenger(tilAnnenForelder(f.annenForelder()), tilRelasjonTilBarn(f.barn(), vedlegg),
                                  tilRettigheter(f.annenForelder()), null,
                                  null, tilUttaksplan(f.uttaksplan(), f.annenForelder(), vedlegg), null);
    }
}

