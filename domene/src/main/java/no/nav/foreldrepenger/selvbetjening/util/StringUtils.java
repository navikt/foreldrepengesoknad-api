package no.nav.foreldrepenger.selvbetjening.util;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class StringUtils {

    private StringUtils() {

    }

    public static String escapeHtml(Object o) {
        if (o == null) {
            return "";
        }
        return escapeHtml4(o.toString());

    }
}
