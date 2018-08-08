package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.UttaksplanPeriode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FordelingDto {

    public List<FordelingPeriodeDto> perioder = new ArrayList<>();
    public Boolean erAnnenForelderInformert;

    public FordelingDto(List<UttaksplanPeriode> uttaksplanperioder, Boolean annenForelderErInformert) {
        for (UttaksplanPeriode u : uttaksplanperioder) {
            perioder.add(new FordelingPeriodeDto(u));
        }
        this.erAnnenForelderInformert = annenForelderErInformert;
    }

    public class FordelingPeriodeDto {

        public String type;
        public LocalDate fom;
        public LocalDate tom;
        public String årsak;
        public String uttaksperiodeType;

        public FordelingPeriodeDto(UttaksplanPeriode u) {
            this.type = u.type;
            this.uttaksperiodeType = u.konto;
            this.fom = u.tidsperiode.fom;
            this.tom = u.tidsperiode.tom;
            this.årsak = u.årsak;
        }
    }
}
