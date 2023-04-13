package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

@JsonInclude(NON_NULL)
public record AnnenInntektFrontend(@Pattern(regexp = "^[\\p{L}_]*$") String type,       // TODO: AnnenOpptjeningType enum, men håndtere ikke null.
                                   @Pattern(regexp = BARE_BOKSTAVER) String land,
                                   @Pattern(regexp = FRITEKST) String arbeidsgiverNavn,
                                   @Valid Tidsperiode tidsperiode,
                                   boolean erNærVennEllerFamilieMedArbeisdgiver,
                                   @Valid @Size(max = 15) List<VedleggReferanse> vedlegg) {

    public AnnenInntektFrontend(String type, String land, String arbeidsgiverNavn, Tidsperiode tidsperiode,
                                boolean erNærVennEllerFamilieMedArbeisdgiver, List<VedleggReferanse> vedlegg) {
        this.type = type;
        this.land = land;
        this.arbeidsgiverNavn = arbeidsgiverNavn;
        this.tidsperiode = tidsperiode;
        this.erNærVennEllerFamilieMedArbeisdgiver = erNærVennEllerFamilieMedArbeisdgiver;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
