package no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Pattern;

public record AnnenforelderDto(Boolean kanIkkeOppgis,
                               @Pattern(regexp = FRITEKST) String fornavn,
                               @Pattern(regexp = FRITEKST) String etternavn,
                               @Pattern(regexp = FRITEKST) String fnr,
                               @Pattern(regexp = BARE_BOKSTAVER) String bostedsland,
                               Boolean utenlandskFnr,
                               boolean harRettPåForeldrepenger,
                               boolean erInformertOmSøknaden,
                               Boolean harMorUføretrygd,
                               Boolean harAnnenForelderOppholdtSegIEØS,
                               Boolean harAnnenForelderTilsvarendeRettEØS) {

    @JsonIgnore
    public Type type() {
        if (Boolean.TRUE.equals(kanIkkeOppgis)) {
            return Type.IKKE_OPPGITT;
        }
        if (Boolean.TRUE.equals(utenlandskFnr)) {
            return Type.UTENLANDSK;
        }
        return Type.NORSK;
    }

    public enum Type {
        NORSK,
        UTENLANDSK,
        IKKE_OPPGITT
    }
}
