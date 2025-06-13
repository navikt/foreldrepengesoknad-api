package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;

public record AnnenInntektDto(@NotNull AnnenOpptjeningType type,
                              @NotNull LocalDate fom,
                              LocalDate tom,
                              CountryCode land,
                              @Pattern(regexp = FRITEKST) String arbeidsgiverNavn) {

    @AssertTrue(message = "Ved JOBB_I_UTLANDET så må land og arbeidsgiverNavn være satt")
    boolean erGyldigJobbIUtlandet() {
        if (AnnenOpptjeningType.JOBB_I_UTLANDET.equals(type)) {
            return land != null && arbeidsgiverNavn != null;
        }
        return true;
    }
}
