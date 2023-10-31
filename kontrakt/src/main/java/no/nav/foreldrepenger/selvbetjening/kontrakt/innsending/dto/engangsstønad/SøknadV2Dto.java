package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;

public interface SøknadV2Dto extends Innsending {
    BarnDto barn();
    UtenlandsoppholdDto utenlandsopphold();
    List<VedleggDto> vedlegg();
    default String navn() {
        if (this instanceof EngangsstønadV2Dto) return "engangsstønad";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp, svp eller es!");
    }
}
