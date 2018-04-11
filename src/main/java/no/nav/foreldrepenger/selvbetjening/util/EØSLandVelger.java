package no.nav.foreldrepenger.selvbetjening.util;

import static com.neovisionaries.i18n.CountryCode.AT;
import static com.neovisionaries.i18n.CountryCode.BE;
import static com.neovisionaries.i18n.CountryCode.BG;
import static com.neovisionaries.i18n.CountryCode.CY;
import static com.neovisionaries.i18n.CountryCode.CZ;
import static com.neovisionaries.i18n.CountryCode.DE;
import static com.neovisionaries.i18n.CountryCode.EE;
import static com.neovisionaries.i18n.CountryCode.ES;
import static com.neovisionaries.i18n.CountryCode.HR;
import static com.neovisionaries.i18n.CountryCode.IE;
import static com.neovisionaries.i18n.CountryCode.IS;
import static com.neovisionaries.i18n.CountryCode.IT;
import static com.neovisionaries.i18n.CountryCode.LI;
import static com.neovisionaries.i18n.CountryCode.LT;
import static com.neovisionaries.i18n.CountryCode.LU;
import static com.neovisionaries.i18n.CountryCode.LV;
import static com.neovisionaries.i18n.CountryCode.MT;
import static com.neovisionaries.i18n.CountryCode.NL;
import static com.neovisionaries.i18n.CountryCode.NO;
import static com.neovisionaries.i18n.CountryCode.PL;
import static com.neovisionaries.i18n.CountryCode.PT;
import static com.neovisionaries.i18n.CountryCode.RO;
import static com.neovisionaries.i18n.CountryCode.SE;
import static com.neovisionaries.i18n.CountryCode.SI;
import static com.neovisionaries.i18n.CountryCode.SK;
import static com.neovisionaries.i18n.CountryCode.UK;

import java.util.Arrays;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

public final class EØSLandVelger {

    private static final List<CountryCode> EØSLAND = Arrays.asList(AT, BE, BG, HR, CY, CZ, DE, EE, IE, IT, LV, LT, LU,
            MT, NL, PL, PT, RO, SK, SI, ES, SE, UK, NO, IS, LI);

    private EØSLandVelger() {

    }

    public static boolean erAnnetEØSLand(CountryCode cc) {
        return cc != null ? EØSLAND.contains(cc) : true;
    }

}
