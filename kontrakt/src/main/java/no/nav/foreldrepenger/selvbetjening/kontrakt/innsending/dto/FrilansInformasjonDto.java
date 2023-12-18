package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;

public record FrilansInformasjonDto(LocalDate oppstart,
                                    boolean jobberFremdelesSomFrilans) {
}
