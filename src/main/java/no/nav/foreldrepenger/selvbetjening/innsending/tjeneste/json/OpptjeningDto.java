package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class OpptjeningDto {

    public List<ArbeidsforholdDto> arbeidsforhold;


    @JsonInclude(NON_NULL)
    public class ArbeidsforholdDto {
        public String type;
        public String orgnummer;
        public String arbeidsgiverNavn;
        public ArbeidsforholdPeriodeDto periode;


        @JsonInclude(NON_NULL)
        public class ArbeidsforholdPeriodeDto {
            public LocalDate fom;
            public LocalDate tom;
        }
    }
}
