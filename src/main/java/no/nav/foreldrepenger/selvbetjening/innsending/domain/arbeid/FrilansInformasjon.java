package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import java.time.LocalDate;
import java.util.List;

public record FrilansInformasjon(
        LocalDate oppstart,
        Boolean driverFosterhjem,
        List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
}
