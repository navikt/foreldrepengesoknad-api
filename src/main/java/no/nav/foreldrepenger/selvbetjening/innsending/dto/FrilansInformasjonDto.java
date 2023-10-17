package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

public record FrilansInformasjonDto(LocalDate oppstart,
                                    boolean jobberFremdelesSomFrilans,
                                    boolean driverFosterhjem,
                                    @Valid @Size(max = 15) List<FrilansoppdragDto> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
}
