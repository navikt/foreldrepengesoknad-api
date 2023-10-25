package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;

public record EngangsstønadV2Dto(LocalDate mottattdato,
                                 @NotNull Målform språkkode,
                                 @Valid @NotNull BarnDto barn,
                                 @Valid @NotNull UtenlandsoppholdDto utenlandsopphold,
                                 @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements SøknadV2Dto {

    public EngangsstønadV2Dto {
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
