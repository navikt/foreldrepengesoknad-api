package no.nav.foreldrepenger.selvbetjening.util;

import static com.neovisionaries.i18n.CountryCode.AT;
import static com.neovisionaries.i18n.CountryCode.BE;
import static com.neovisionaries.i18n.CountryCode.BG;
import static com.neovisionaries.i18n.CountryCode.CH;
import static com.neovisionaries.i18n.CountryCode.CY;
import static com.neovisionaries.i18n.CountryCode.CZ;
import static com.neovisionaries.i18n.CountryCode.EE;
import static com.neovisionaries.i18n.CountryCode.ES;
import static com.neovisionaries.i18n.CountryCode.FR;
import static com.neovisionaries.i18n.CountryCode.GR;
import static com.neovisionaries.i18n.CountryCode.HR;
import static com.neovisionaries.i18n.CountryCode.HU;
import static com.neovisionaries.i18n.CountryCode.IE;
import static com.neovisionaries.i18n.CountryCode.IT;
import static com.neovisionaries.i18n.CountryCode.LI;
import static com.neovisionaries.i18n.CountryCode.LT;
import static com.neovisionaries.i18n.CountryCode.LU;
import static com.neovisionaries.i18n.CountryCode.LV;
import static com.neovisionaries.i18n.CountryCode.MT;
import static com.neovisionaries.i18n.CountryCode.NL;
import static com.neovisionaries.i18n.CountryCode.PL;
import static com.neovisionaries.i18n.CountryCode.PT;
import static com.neovisionaries.i18n.CountryCode.RO;
import static com.neovisionaries.i18n.CountryCode.SI;
import static com.neovisionaries.i18n.CountryCode.UK;

import java.util.Arrays;
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
    SLOVENIA(SI), 
    SPANIA(ES), 
    STORBRITANNIAOGNORDIRLAND(UK), 
    SVEITS(CH),
    TSJEKKIA(GR),
    TYSKLAND(CZ),
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
