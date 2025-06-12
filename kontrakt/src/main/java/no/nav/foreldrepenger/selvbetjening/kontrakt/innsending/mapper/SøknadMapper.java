package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.EndringForeldrepengerMapper.tilEndringForeldrepengesøknadUtenVedleggInnhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilForeldrepengesøknadVedleggUtenInnhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.EndringForeldrepengerMapper.tilEndringForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.EngangsstønadMapper.tilEngangsstønad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknad;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;

public final class SøknadMapper {

    private SøknadMapper() {
    }

    public static Søknad tilSøknad(Innsending innsending, LocalDate mottattDato) {
        var påkrevdeVedlegg = innsending.påkrevdeVedlegg();
        if (innsending instanceof SøknadDtoOLD søknad) {
            return tilSøknad(søknad, påkrevdeVedlegg, mottattDato);
        } else if (innsending instanceof EndringssøknadDtoOLD endrringsøknad) {
            return tilEndringssøknad(endrringsøknad, påkrevdeVedlegg, mottattDato);
        } else if (innsending instanceof SøknadDto søknadV2) {
            return tilSøknad(søknadV2, påkrevdeVedlegg, mottattDato);
        } else if (innsending instanceof EndringssøknadDto endrringsøknad) {
            return tilEndringssøknad(endrringsøknad, påkrevdeVedlegg, mottattDato);
        }
        throw new IllegalArgumentException("Utviklerfeil: Ukjent søknad " + innsending.getClass().getSimpleName());
    }

    private static Søknad tilSøknad(SøknadDtoOLD søknad, List<VedleggDto> påkrevdeVedlegg, LocalDate mottattDato) {
        if (søknad instanceof ForeldrepengesøknadDtoOLD f) {
            return tilForeldrepengesøknadVedleggUtenInnhold(f, påkrevdeVedlegg, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + søknad.getClass().getSimpleName());
    }

    private static Endringssøknad tilEndringssøknad(EndringssøknadDtoOLD endringssøknad, List<VedleggDto> påkrevdeVedlegg, LocalDate mottattDato) {
        if (endringssøknad instanceof EndringssøknadForeldrepengerDtoOLD f) {
            return tilEndringForeldrepengesøknadUtenVedleggInnhold(f, påkrevdeVedlegg, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + endringssøknad.getClass().getSimpleName());
    }


    // V2
    private static Søknad tilSøknad(SøknadDto søknad, List<VedleggDto> påkrevdeVedlegg, LocalDate mottattDato) {
        if (søknad instanceof EngangsstønadDto e) {
            return tilEngangsstønad(e, påkrevdeVedlegg, mottattDato);
        }
        if (søknad instanceof ForeldrepengesøknadDto f) {
            return tilForeldrepengesøknad(f, påkrevdeVedlegg, mottattDato);
        }
        if (søknad instanceof SvangerskapspengesøknadDto s) {
            return tilSvangerskapspengesøknad(s, påkrevdeVedlegg, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + søknad.getClass().getSimpleName());
    }

    private static Endringssøknad tilEndringssøknad(EndringssøknadDto endringssøknad, List<VedleggDto> påkrevdeVedlegg, LocalDate mottattDato) {
        if (endringssøknad instanceof EndringssøknadForeldrepengerDto f) {
            return tilEndringForeldrepengesøknad(f, påkrevdeVedlegg, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + endringssøknad.getClass().getSimpleName());
    }
}
