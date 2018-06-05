package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.UttaksplanPeriode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class FordelingDto {

    public List<FordelingPeriodeDto> perioder = new ArrayList<>();

    public FordelingDto(List<UttaksplanPeriode> uttaksplanperioder) {
        for (UttaksplanPeriode u : uttaksplanperioder) {
            perioder.add(new FordelingPeriodeDto(u));
        }
    }

    @JsonInclude(NON_NULL)
    public class FordelingPeriodeDto {

        public String type;
        public LocalDate fom;
        public LocalDate tom;
        public String årsak;

        public FordelingPeriodeDto(UttaksplanPeriode u) {
            this.type = u.type;
            this.fom = u.tidsperiode.startdato;
            this.tom = u.tidsperiode.sluttdato;
            this.årsak = u.årsak;
        }
    }
}
