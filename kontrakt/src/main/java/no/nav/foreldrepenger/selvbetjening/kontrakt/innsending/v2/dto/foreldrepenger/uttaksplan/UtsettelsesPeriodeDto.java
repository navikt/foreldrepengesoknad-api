package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesÅrsak;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record UtsettelsesPeriodeDto(@NotNull Type type,
                                    @NotNull LocalDate fom,
                                    @NotNull LocalDate tom,
                                    @NotNull UtsettelsesÅrsak årsak,
                                    MorsAktivitet morsAktivitetIPerioden,
                                    Boolean erArbeidstaker,
                                    @Valid @Size(max = 100) List<@Valid @NotNull MutableVedleggReferanseDto> vedleggsreferanser) implements Uttaksplanperiode {

    public UtsettelsesPeriodeDto {
        vedleggsreferanser = Optional.ofNullable(vedleggsreferanser).map(ArrayList::new).orElse(new ArrayList<>());
    }

    public enum Type {
        UTSETTELSE,
        FRI
    }
}
