package no.nav.foreldrepenger.selvbetjening.consumer.json;

import java.time.LocalDate;

public class YtelseDto {

    public String type;
    public MedlemsskapDto medlemsskap;
    public RelasjonTilBarn relasjonTilBarn;

    public YtelseDto() {
        this.medlemsskap = new MedlemsskapDto();
        this.relasjonTilBarn = new RelasjonTilBarn();
    }

    public class MedlemsskapDto {
        public Boolean norgeSiste12;
        public Boolean norgeNeste12;
        public Boolean fødselNorge;
        public String arbeidSiste12;
    }

    public class RelasjonTilBarn {
        public String type;
        public Integer antallBarn;
        public LocalDate terminDato;
        public LocalDate utstedtDato;
    }

}
