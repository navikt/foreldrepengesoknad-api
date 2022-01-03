package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public record FrilansInformasjon(LocalDate oppstart,
                                 boolean driverFosterhjem,
                                 List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd) {

    @JsonCreator
    public FrilansInformasjon(LocalDate oppstart, boolean driverFosterhjem, List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
        this.oppstart = oppstart;
        this.driverFosterhjem = driverFosterhjem;
        this.oppdragForNæreVennerEllerFamilieSiste10Mnd = Optional.ofNullable(oppdragForNæreVennerEllerFamilieSiste10Mnd).orElse(emptyList());
    }
}
