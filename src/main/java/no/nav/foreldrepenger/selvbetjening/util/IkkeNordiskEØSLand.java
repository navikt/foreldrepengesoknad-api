package no.nav.foreldrepenger.selvbetjening.util;

import java.util.Arrays;

import static com.neovisionaries.i18n.CountryCode.*;
import static java.util.stream.Collectors.toList;

import com.neovisionaries.i18n.CountryCode;

public enum IkkeNordiskEØSLand {

    BELGIA(BE), 
    BULGARIA(BG), 
    ESTLAND(EE),
    FRANKRIKE(FR), 
    HELLAS(GR), 
    IRLAND(IE), 
    ITALIA(IT), 
    KROATIA(HR), 
    KYPROS(CY), 
    LATVIA(LV), 
    LICHTENSTEIN(LI), 
    LITAUEN(LT), 
    LUXEMBURG(LU),
    MALTA(MT),
    NEDERLAND(NL),
    POLEN(PL),
    PORTUGAL(PT),
    ROMANIA(RO),
    SLOVAKIA(SK),
    SLOVENIA(SI), 
    SPANIA(ES), 
    STORBRITANNIAOGNORDIRLAND(UK), 
    SVEITS(CH),
    TSJEKKIA(CZ),
    TYSKLAND(DE),
    UNGARN(HU),
    ØSTERRIKE(AT);

    private final CountryCode land;

    IkkeNordiskEØSLand(CountryCode land) {
        this.land = land;
    }

    public static boolean ikkeNordiskEøsLand(CountryCode landKode) {
        return Arrays.stream(values())
                .map(cc -> cc.land)
                .collect(toList())
                .contains(landKode);
    }

}
