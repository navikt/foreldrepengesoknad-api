package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;

public interface SøknadDto extends Innsending {
    BarnDto barn();
    UtenlandsoppholdDto utenlandsopphold();
    List<VedleggDto> vedlegg();
    default String navn() {
        if (this instanceof EngangsstønadDto) return "engangsstønad";
        if (this instanceof ForeldrepengesøknadDto) return "foreldrepenger";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp, svp eller es!");
    }
}
