package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record AnnenInntekt(
        String type,
        String land,
        String arbeidsgiverNavn,
        Tidsperiode tidsperiode,
        Boolean erNærVennEllerFamilieMedArbeisdgiver,
        List<String> vedlegg) {
}
