package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

@JsonInclude(NON_NULL)
public record AnnenInntekt(String type,
                           String land,
                           String arbeidsgiverNavn,
                           Tidsperiode tidsperiode,
                           boolean erNærVennEllerFamilieMedArbeisdgiver,
                           List<String> vedlegg) {

    public AnnenInntekt(String type, String land, String arbeidsgiverNavn, Tidsperiode tidsperiode,
                        boolean erNærVennEllerFamilieMedArbeisdgiver, List<String> vedlegg) {
        this.type = type;
        this.land = land;
        this.arbeidsgiverNavn = arbeidsgiverNavn;
        this.tidsperiode = tidsperiode;
        this.erNærVennEllerFamilieMedArbeisdgiver = erNærVennEllerFamilieMedArbeisdgiver;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
