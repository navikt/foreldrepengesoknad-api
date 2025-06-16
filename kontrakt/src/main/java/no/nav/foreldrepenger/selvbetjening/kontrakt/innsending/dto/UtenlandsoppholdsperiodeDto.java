package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record UtenlandsoppholdsperiodeDto(@NotNull LocalDate fom, @NotNull LocalDate tom, @NotNull CountryCode landkode) {

    @AssertTrue(message = "FOM dato må være etter TOM dato!")
    public boolean isFomAfterTom() { //NOSONAR. Må starte med is/has/get for at AssertTrue skal fungere
        return fom().isBefore(tom());
    }
}
