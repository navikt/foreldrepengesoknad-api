package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neovisionaries.i18n.CountryCode;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AnnenInntektDto.Annet.class, names = { "ETTERLØNN_SLUTTPAKKE", "MILITÆR_ELLER_SIVILTJENESTE" }),
    @JsonSubTypes.Type(value = AnnenInntektDto.Utlandet.class, name = "JOBB_I_UTLANDET")
})
public interface AnnenInntektDto {
    LocalDate fom();
    LocalDate tom();

    record Annet(AnnenOpptjeningType type, LocalDate fom, LocalDate tom) implements AnnenInntektDto {
    }

    record Utlandet(CountryCode land, @Pattern(regexp = FRITEKST) String arbeidsgiverNavn, LocalDate fom, LocalDate tom) implements AnnenInntektDto {
    }
}
