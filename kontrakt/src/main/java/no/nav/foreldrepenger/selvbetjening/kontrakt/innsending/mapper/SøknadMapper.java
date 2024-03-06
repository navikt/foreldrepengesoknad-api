package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.EndringForeldrepengerMapper.tilEndringForeldrepengesøknadUtenVedleggInnhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.ForeldrepengerMapper.tilForeldrepengesøknadVedleggUtenInnhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknadVedleggUtenInnhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.EndringForeldrepengerMapper.tilEndringForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.EngangsstønadMapper.tilEngangsstønad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.ForeldrepengerMapper.tilForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknad;

import java.time.LocalDate;

import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;

public final class SøknadMapper {

    private SøknadMapper() {
    }

    public static Søknad tilSøknad(Innsending innsending, LocalDate mottattDato) {
        if (innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto søknad) {
            return tilSøknad(søknad, mottattDato);
        } else if (innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto endrringsøknad) {
            return tilEndringssøknad(endrringsøknad, mottattDato);
        } else if (innsending instanceof SøknadDto søknadV2) {
            return tilSøknad(søknadV2, mottattDato);
        } else if (innsending instanceof EndringssøknadDto endrringsøknad) {
            return tilEndringssøknad(endrringsøknad, mottattDato);
        }
        throw new IllegalArgumentException("Utviklerfeil: Ukjent søknad " + innsending.getClass().getSimpleName());
    }

    private static Søknad tilSøknad(no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto søknad, LocalDate mottattDato) {
        if (søknad instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto f) {
            return tilForeldrepengesøknadVedleggUtenInnhold(f, mottattDato);
        }
        if (søknad instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto s) {
            return tilSvangerskapspengesøknadVedleggUtenInnhold(s, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + søknad.getClass().getSimpleName());
    }

    private static Endringssøknad tilEndringssøknad(no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto endringssøknad, LocalDate mottattDato) {
        if (endringssøknad instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto f) {
            return tilEndringForeldrepengesøknadUtenVedleggInnhold(f, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + endringssøknad.getClass().getSimpleName());
    }





    // V2
    private static Søknad tilSøknad(SøknadDto søknad, LocalDate mottattDato) {
        if (søknad instanceof EngangsstønadDto e) {
            return tilEngangsstønad(e, mottattDato);
        }
        if (søknad instanceof ForeldrepengesøknadDto f) {
            return tilForeldrepengesøknad(f, mottattDato);
        }
        if (søknad instanceof SvangerskapspengesøknadDto s) {
            return tilSvangerskapspengesøknad(s, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + søknad.getClass().getSimpleName());
    }

    private static Endringssøknad tilEndringssøknad(EndringssøknadDto endringssøknad, LocalDate mottattDato) {
        if (endringssøknad instanceof EndringssøknadForeldrepengerDto f) {
            return tilEndringForeldrepengesøknad(f, mottattDato);
        }
        throw new IllegalArgumentException("Ukjent søknad " + endringssøknad.getClass().getSimpleName());
    }
}
