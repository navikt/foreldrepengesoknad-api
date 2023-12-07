package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AnnenInntektDto(@Pattern(regexp = "^[\\p{L}_]*$") String type,
                              @Pattern(regexp = BARE_BOKSTAVER) String land,
                              @Pattern(regexp = FRITEKST) String arbeidsgiverNavn,
                              @Valid ÅpenPeriodeDto tidsperiode,
                              boolean erNærVennEllerFamilieMedArbeisdgiver,
                              @Valid @Size(max = 15) List<MutableVedleggReferanseDto> vedlegg) {

    public AnnenInntektDto {
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
