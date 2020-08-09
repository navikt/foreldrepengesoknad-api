package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_CALL_ID;

import java.util.Optional;

import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class MDCUtil {

    public static final Marker CONFIDENTIAL = MarkerFactory.getMarker("CONFIDENTIAL");

    private MDCUtil() {

    }

    public static String callId() {
        return MDC.get(NAV_CALL_ID);
    }

    public static void toMDC(String key, Object value) {
        if (value != null) {
            toMDC(key, value.toString());
        }
    }

    public static void toMDC(String key, String value) {
        toMDC(key, value, null);
    }

    public static void toMDC(String key, String value, String defaultValue) {
        MDC.put(key, Optional.ofNullable(value)
                .orElse(defaultValue));
    }
}
