package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.time.LocalDate;
import java.util.List;

public class FrilansInformasjon {

    public LocalDate oppstart;
    public Boolean jobberFremdelesSomFrilans;
    public Boolean driverFosterhjem;
    public Boolean harJobbetForNærVennEllerFamilieSiste10Mnd;
    public List<Frilansoppdrag> oppdragForNæreVennerEllerFamilieSiste10Mnd;

}
