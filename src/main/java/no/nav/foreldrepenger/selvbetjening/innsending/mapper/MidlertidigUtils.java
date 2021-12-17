package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

import com.neovisionaries.i18n.CountryCode;

// TODO: Fjern disse innslagene når kontrakten mellom api og frontend er strict (bruk primitive og objekter)
final class MidlertidigUtils {

    private MidlertidigUtils() {
    }

    public static int tilInt(Integer integer) {
        return integer != null ? integer : 0;
    }

    public static long tilLong(Integer integer) {
        return integer != null ? integer : 0;
    }

    public static CountryCode land(String land) {
        return isNullOrEmpty(land) ? null : CountryCode.valueOf(land);
    }
}
