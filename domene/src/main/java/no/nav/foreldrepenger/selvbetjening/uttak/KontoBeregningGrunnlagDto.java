package no.nav.foreldrepenger.selvbetjening.uttak;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record KontoBeregningGrunnlagDto(@NotNull @Digits(integer = 2, fraction = 0) int antallBarn,
                                        @NotNull Boolean morHarRett,
                                        @NotNull Boolean farHarRett,
                                        @NotNull Boolean morHarAleneomsorg,
                                        @NotNull Boolean farHarAleneomsorg,
                                        LocalDate fødselsdato,
                                        LocalDate termindato,
                                        LocalDate omsorgsovertakelseDato,
                                        @NotNull Boolean erMor,
                                        @NotNull Boolean minsterett,
                                        @NotNull Boolean morHarUføretrygd,
                                        @NotNull Boolean harAnnenForelderTilsvarendeRettEØS,
                                        LocalDate familieHendelseDatoNesteSak) {

}
