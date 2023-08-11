package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.EngangsstønadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ForeldrepengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SvangerskapspengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EngangsstønadMapper.tilEngangsstønad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.ForeldrepengerMapper.tilForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknad;

public final class SøknadMapper {

    private SøknadMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.Søknad tilSøknad(SøknadFrontend søknadFrontend) {
        if (søknadFrontend instanceof EngangsstønadFrontend s) {
            return tilEngangsstønad(s);
        }
        if (søknadFrontend instanceof ForeldrepengesøknadFrontend s) {
            return tilForeldrepengesøknad(s);
        }
        if (søknadFrontend instanceof SvangerskapspengesøknadFrontend s) {
            return tilSvangerskapspengesøknad(s);
        }
        throw new IllegalArgumentException("Ukjent søknad " + søknadFrontend.getClass().getSimpleName());
    }
}
