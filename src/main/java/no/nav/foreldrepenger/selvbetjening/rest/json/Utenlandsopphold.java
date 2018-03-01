package no.nav.foreldrepenger.selvbetjening.rest.json;

import java.time.LocalDate;
import java.util.List;

public class Utenlandsopphold {

    public Boolean fødselINorge;
    public Boolean iNorgeNeste12Mnd;
    public Boolean iNorgeSiste12Mnd;
    public Boolean jobbetINorgeSiste12Mnd;
    
    public List<Periode> perioder;

    public class Periode {
        public LocalDate fom;
        public LocalDate tom;
    }

}
