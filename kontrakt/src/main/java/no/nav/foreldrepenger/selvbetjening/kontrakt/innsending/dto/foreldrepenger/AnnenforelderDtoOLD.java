package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Pattern;

public record AnnenforelderDtoOLD(Boolean kanIkkeOppgis,
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
    public AnnenForelderType type() {
        if (Boolean.TRUE.equals(kanIkkeOppgis)) {
            return AnnenForelderType.IKKE_OPPGITT;
        }
        if (Boolean.TRUE.equals(utenlandskFnr)) {
            return AnnenForelderType.UTENLANDSK;
        }
        return AnnenForelderType.NORSK;
    }

    public enum AnnenForelderType {
        NORSK,
        UTENLANDSK,
        IKKE_OPPGITT
    }
}
