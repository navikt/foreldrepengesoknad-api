package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.EndringForeldrepengerMapper.tilEndringForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.EngangsstønadMapper.tilEngangsstønad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknad;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;

public final class SøknadMapper {

    private SøknadMapper() {
    }

    public static Søknad tilSøknad(Innsending innsending, LocalDate mottattDato) {
        var påkrevdeVedlegg = innsending.påkrevdeVedlegg();
        if (innsending instanceof SøknadDto søknadV2) {
            return tilSøknad(søknadV2, påkrevdeVedlegg, mottattDato);
        } else if (innsending instanceof EndringssøknadDto endrringsøknad) {
            return tilEndringssøknad(endrringsøknad, påkrevdeVedlegg, mottattDato);
        }
        throw new IllegalArgumentException("Utviklerfeil: Ukjent søknad " + innsending.getClass().getSimpleName());
    }

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
