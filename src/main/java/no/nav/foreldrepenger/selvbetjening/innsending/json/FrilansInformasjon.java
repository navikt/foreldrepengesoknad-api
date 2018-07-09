package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FrilansInformasjon {

    public LocalDate oppstart;
    public Boolean driverFosterhjem;
    public List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd = new ArrayList<>();

}
