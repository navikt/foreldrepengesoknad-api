package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

@JsonInclude(NON_NULL)
public record AnnenInntektFrontend(@Pattern(regexp = FRITEKST) String type,
                                   @Pattern(regexp = FRITEKST) String land,
                                   @Pattern(regexp = FRITEKST) String arbeidsgiverNavn,
                                   Tidsperiode tidsperiode,
                                   boolean erNærVennEllerFamilieMedArbeisdgiver,
                                   List<@Pattern(regexp = FRITEKST) String> vedlegg) {

    public AnnenInntektFrontend(String type, String land, String arbeidsgiverNavn, Tidsperiode tidsperiode,
                                boolean erNærVennEllerFamilieMedArbeisdgiver, List<String> vedlegg) {
        this.type = type;
        this.land = land;
        this.arbeidsgiverNavn = arbeidsgiverNavn;
        this.tidsperiode = tidsperiode;
        this.erNærVennEllerFamilieMedArbeisdgiver = erNærVennEllerFamilieMedArbeisdgiver;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
