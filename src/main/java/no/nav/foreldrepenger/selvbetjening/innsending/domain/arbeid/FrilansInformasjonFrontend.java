package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;

public record FrilansInformasjonFrontend(LocalDate oppstart,
                                         boolean jobberFremdelesSomFrilans,
                                         boolean driverFosterhjem,
                                         @Valid @Size(max = 15) List<FrilansoppdragFrontend> oppdragForNæreVennerEllerFamilieSiste10Mnd) {

    @JsonCreator
    public FrilansInformasjonFrontend(LocalDate oppstart, boolean jobberFremdelesSomFrilans, boolean driverFosterhjem, List<FrilansoppdragFrontend> oppdragForNæreVennerEllerFamilieSiste10Mnd) {
        this.oppstart = oppstart;
        this.jobberFremdelesSomFrilans = jobberFremdelesSomFrilans;
        this.driverFosterhjem = driverFosterhjem;
        this.oppdragForNæreVennerEllerFamilieSiste10Mnd = Optional.ofNullable(oppdragForNæreVennerEllerFamilieSiste10Mnd).orElse(emptyList());
    }
}
