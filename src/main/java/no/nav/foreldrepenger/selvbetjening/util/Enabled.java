package no.nav.foreldrepenger.selvbetjening.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Enabled {

    public static boolean TPSBARN;
    public static boolean SVANGERSKAPSPENGER;
    public static boolean FNR_HEADER_FILTER;

    public Enabled(
            @Value("${TOGGLES_ENABLE_TPSBARN:false}") boolean tpsBarn,
            @Value("${TOGGLES_ENABLE_SVANGERSKAPSPENGER:false}") boolean svangerskapspenger,
            @Value("${TOGGLES_FNR_HEADER_FILTER:false}") boolean fnrHeaderFilter
            ) {
        Enabled.TPSBARN = tpsBarn;
        Enabled.SVANGERSKAPSPENGER = svangerskapspenger;
        Enabled.FNR_HEADER_FILTER = fnrHeaderFilter;
    }
}
