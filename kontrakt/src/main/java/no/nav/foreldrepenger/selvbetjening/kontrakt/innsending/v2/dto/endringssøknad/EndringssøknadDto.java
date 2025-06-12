package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad;

import java.util.List;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

public interface EndringssøknadDto extends Innsending {
    Saksnummer saksnummer();

    BarnDto barn();

    List<VedleggDto> vedlegg();

    default String navn() {
        if (this instanceof EndringssøknadForeldrepengerDto) {
            return "endringssøknad foreldrepenger";
        }
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp!");
    }
}
