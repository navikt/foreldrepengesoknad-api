package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilAnnenForelder;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilLukketPeriodeMedVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilRettigheter;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilSøker;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.Fordeling;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

final class EndringForeldrepengerMapper {

    private EndringForeldrepengerMapper() {
    }

    static Endringssøknad tilEndringForeldrepengesøknadUtenVedleggInnhold(EndringssøknadForeldrepengerDto endringssøknadFP,
                                                                          List<VedleggDto> påkrevdeVedlegg,
                                                                          LocalDate mottattDato) {
        return new Endringssøknad(mottattDato, tilSøker(endringssøknadFP.søker()), tilYtelse(endringssøknadFP, påkrevdeVedlegg),
            endringssøknadFP.tilleggsopplysninger(), tilVedlegg(påkrevdeVedlegg), endringssøknadFP.saksnummer());
    }

    private static Foreldrepenger tilYtelse(EndringssøknadForeldrepengerDto f, List<VedleggDto> vedlegg) {
        return new Foreldrepenger(tilAnnenForelder(f.annenForelder()), tilRelasjonTilBarn(f.barn(), f.situasjon(), vedlegg),
            tilRettigheter(f.søker(), f.annenForelder()), null, null, tilFordeling(f, vedlegg), null);
    }

    private static Fordeling tilFordeling(EndringssøknadForeldrepengerDto f, List<VedleggDto> vedlegg) {
        return new Fordeling(f.annenForelder().erInformertOmSøknaden(), tilLukketPeriodeMedVedlegg(f.uttaksplan(), vedlegg),
            f.ønskerJustertUttakVedFødsel());
    }
}

