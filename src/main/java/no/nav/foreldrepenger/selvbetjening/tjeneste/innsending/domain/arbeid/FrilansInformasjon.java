package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FrilansInformasjon {

    public LocalDate oppstart;
    public Boolean driverFosterhjem;
    public List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd = new ArrayList<>();

}
