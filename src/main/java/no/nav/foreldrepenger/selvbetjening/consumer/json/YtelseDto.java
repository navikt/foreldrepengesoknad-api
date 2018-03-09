package no.nav.foreldrepenger.selvbetjening.consumer.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class YtelseDto {

    public String type;
    public MedlemsskapDto medlemsskap;
    public RelasjonTilBarn relasjonTilBarn;

    public YtelseDto() {
        this.medlemsskap = new MedlemsskapDto();
        this.relasjonTilBarn = new RelasjonTilBarn();
    }

    @JsonInclude(NON_NULL)
    public class MedlemsskapDto {
        public Boolean norgeSiste12;
        public Boolean norgeNeste12;
        public Boolean fødselNorge;
        public String arbeidSiste12;
    }

    @JsonInclude(NON_NULL)
    public class RelasjonTilBarn {
        public String type;
        public Integer antallBarn;
        public LocalDate terminDato;
        public LocalDate utstedtDato;
        public LocalDate fødselsdato;
    }

}
