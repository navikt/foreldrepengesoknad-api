package no.nav.foreldrepenger.selvbetjening.uttak;

import java.time.LocalDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Brukerrolle;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Rettighetstype;

public record KontoBeregningGrunnlagDto(@NotNull Rettighetstype rettighetstype,
                                        @NotNull Brukerrolle brukerrolle,
                                        @NotNull @Digits(integer = 2, fraction = 0) int antallBarn,
                                        LocalDate fødselsdato,
                                        LocalDate termindato,
                                        LocalDate omsorgsovertakelseDato,
                                        boolean morHarUføretrygd,
                                        LocalDate familieHendelseDatoNesteSak) {
}
