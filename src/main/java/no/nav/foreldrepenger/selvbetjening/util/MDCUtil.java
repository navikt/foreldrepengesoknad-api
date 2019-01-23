package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_CALL_ID;

import java.util.Optional;

import org.slf4j.MDC;

public final class MDCUtil {
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
        MDC.put(key, Optional.ofNullable(value).orElse(defaultValue));
    }
}
