package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.Frilansoppdrag;

@JsonInclude(NON_EMPTY)
public class FrilansDto {
    @Override
    public String toString() {
        return "FrilansDto [periode=" + periode + ", harInntektFraFosterhjem=" + harInntektFraFosterhjem
                + ", frilansOppdrag=" + frilansOppdrag + "]";
    }

    public PeriodeDto periode;
    public Boolean harInntektFraFosterhjem;
    public Boolean nyOppstartet;
    public List<FrilansoppdragDto> frilansOppdrag = new ArrayList<>();

    public FrilansDto(FrilansInformasjon frilansInformasjon) {
        this.periode = PeriodeDto.open(frilansInformasjon.getOppstart());
        this.harInntektFraFosterhjem = frilansInformasjon.getDriverFosterhjem();

        LocalDate treMånederFørFom = now().minus(Period.ofDays(90));
        this.nyOppstartet = this.periode.fom().isAfter(treMånederFørFom);

        for (Frilansoppdrag o : frilansInformasjon.getOppdragForNæreVennerEllerFamilieSiste10Mnd()) {
            frilansOppdrag.add(new FrilansoppdragDto(o.getNavnPåArbeidsgiver(), o.getTidsperiode().getFom(),
                    o.getTidsperiode().getTom()));
        }
    }
}