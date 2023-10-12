package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EndringForeldrepengerMapper.tilEndringForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EngangsstønadMapper.tilEngangsstønad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.ForeldrepengerMapper.tilForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknad;

import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;

public final class SøknadMapper {

    private SøknadMapper() {
    }

    public static Søknad tilSøknad(SøknadDto søknad) {
        if (søknad instanceof ForeldrepengesøknadDto f) {
            return tilForeldrepengesøknad(f);
        }
        if (søknad instanceof EngangsstønadDto e) {
            return tilEngangsstønad(e);
        }
        if (søknad instanceof SvangerskapspengesøknadDto s) {
            return tilSvangerskapspengesøknad(s);
        }
        throw new IllegalArgumentException("Ukjent søknad " + søknad.getClass().getSimpleName());
    }

    public static Endringssøknad tilEndringssøknad(EndringssøknadDto endringssøknad) {
        if (endringssøknad instanceof EndringssøknadForeldrepengerDto f) {
            return tilEndringForeldrepengesøknad(f);
        }
        throw new IllegalArgumentException("Ukjent søknad " + endringssøknad.getClass().getSimpleName());
    }
}
