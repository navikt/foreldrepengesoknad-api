package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Frilansoppdrag;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class OpptjeningDto {

    public FrilansDto frilans;

    public OpptjeningDto(Søker søker) {
        if (søker.frilansInformasjon != null) {
            this.frilans = new FrilansDto(søker.frilansInformasjon);
        }
    }

    @JsonInclude(NON_EMPTY)
    public class FrilansDto {
        public PeriodeDto periode = new PeriodeDto();
        public Boolean harInntektFraFosterhjem;
        public List<FrilansoppdragDto> frilansOppdrag = new ArrayList<>();

        public FrilansDto(FrilansInformasjon frilansInformasjon) {
            this.periode.fom = frilansInformasjon.oppstart;
            this.harInntektFraFosterhjem = frilansInformasjon.driverFosterhjem;

            for (Frilansoppdrag o : frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd) {
                frilansOppdrag.add(new FrilansoppdragDto(o.navnPåArbeidsgiver, o.tidsperiode.startdato, o.tidsperiode.sluttdato));
            }
        }
    }

    public class FrilansoppdragDto {
        public String oppdragsgiver;
        public PeriodeDto periode = new PeriodeDto();

        public FrilansoppdragDto(String oppdragsgiver, LocalDate fom, LocalDate tom) {
            this.oppdragsgiver = oppdragsgiver;
            this.periode.fom = fom;
            this.periode.tom = tom;
        }
    }

    @JsonInclude(NON_NULL)
    public class PeriodeDto {
        public LocalDate fom;
        public LocalDate tom;
    }
}
