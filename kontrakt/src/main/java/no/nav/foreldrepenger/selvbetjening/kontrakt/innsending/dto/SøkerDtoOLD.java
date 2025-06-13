package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;

public record SøkerDtoOLD(BrukerRolle rolle, Målform språkkode, boolean erAleneOmOmsorg, @Valid FrilansInformasjonDtoOLD frilansInformasjon,
                          @Valid @Size(max = 15) List<@Valid @NotNull NæringDtoOLD> selvstendigNæringsdrivendeInformasjon,
                          @Valid @Size(max = 15) List<@Valid @NotNull AnnenInntektDtoOLD> andreInntekterSiste10Mnd) {
    public SøkerDtoOLD {
        selvstendigNæringsdrivendeInformasjon = Optional.ofNullable(selvstendigNæringsdrivendeInformasjon).orElse(emptyList());
        andreInntekterSiste10Mnd = Optional.ofNullable(andreInntekterSiste10Mnd).orElse(emptyList());
    }
}
