package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;

public record UtsettelsesPeriodeDto(@NotNull Type type,
                                    @NotNull LocalDate fom,
                                    @NotNull LocalDate tom,
                                    @NotNull UtsettelsesÅrsak årsak,
                                    MorsAktivitet morsAktivitetIPerioden,
                                    boolean erArbeidstaker) implements Uttaksplanperiode {

    public enum Type {
        UTSETTELSE,
        FRI
    }
}
