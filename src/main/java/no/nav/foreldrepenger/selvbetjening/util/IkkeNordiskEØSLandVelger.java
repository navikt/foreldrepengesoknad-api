package no.nav.foreldrepenger.selvbetjening.util;

import static com.neovisionaries.i18n.CountryCode.AT;
import static com.neovisionaries.i18n.CountryCode.BE;
import static com.neovisionaries.i18n.CountryCode.BG;
import static com.neovisionaries.i18n.CountryCode.CH;
import static com.neovisionaries.i18n.CountryCode.CY;
import static com.neovisionaries.i18n.CountryCode.CZ;
import static com.neovisionaries.i18n.CountryCode.DE;
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
import static com.neovisionaries.i18n.CountryCode.SK;
import static com.neovisionaries.i18n.CountryCode.UK;

import java.util.Arrays;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

public final class IkkeNordiskEØSLandVelger {

    private static final List<CountryCode> IKKENORDISKEØSLAND = Arrays.asList(
            BE, /* Belgia */
            BG, /* Bulgaria */
            EE, /* Estland */
            FR, /* Frankrike */
            GR, /* Hellas */
            IE, /* Irland */
            IT, /* Italia */
            HR, /* Kroatia */
            CY, /* Kypros */
            LV, /* Latvia */
            LI, /* Lichtenstein */
            LT, /* Litauen */
            LU, /* Luxemburg */
            MT, /* Malta */
            NL, /* Nederland */
            PL, /* Polen */
            PT, /* Portugal */
            RO, /* Romania */
            SK, /* Slovakia */
            SI, /* Slovenia */
            ES, /* Spania */
            UK, /* Storbritannia og Nord Irland */
            CH, /* Sveits */
            CZ, /* Tsjekkia */
            DE, /* Tyskland */
            HU, /* Ungarn */
            AT /* Østerrike */
    );

    private IkkeNordiskEØSLandVelger() {

    }

    public static boolean erIkkenordiskEØSLand(CountryCode cc) {
        return IKKENORDISKEØSLAND.contains(cc);
    }

}
