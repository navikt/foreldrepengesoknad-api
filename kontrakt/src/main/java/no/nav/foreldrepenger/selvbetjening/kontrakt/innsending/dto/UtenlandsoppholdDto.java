package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.neovisionaries.i18n.CountryCode;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UtenlandsoppholdDto(@Valid @Size(max = 20) List<@Valid @NotNull Periode> utenlandsoppholdSiste12Mnd,
                                  @Valid @Size(max = 20) List<@Valid @NotNull Periode> utenlandsoppholdNeste12Mnd) {

    public UtenlandsoppholdDto {
        utenlandsoppholdSiste12Mnd = Optional.ofNullable(utenlandsoppholdSiste12Mnd).orElse(emptyList());
        utenlandsoppholdNeste12Mnd = Optional.ofNullable(utenlandsoppholdNeste12Mnd).orElse(emptyList());
    }

    public record Periode(@NotNull LocalDate fom, @NotNull LocalDate tom, @NotNull CountryCode landkode) {
        @AssertTrue(message = "FOM dato må være etter TOM dato!")
        public boolean isFomAfterTom() { //NOSONAR. Må starte med is/has/get for at AssertTrue skal fungere
            return fom.isBefore(tom);
        }
    }
}
