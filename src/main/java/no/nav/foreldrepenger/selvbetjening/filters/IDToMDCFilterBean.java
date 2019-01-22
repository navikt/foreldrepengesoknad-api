package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_AKTØR_ID;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_USER_ID;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.isDevOrPreprod;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.Oppslag;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Order(LOWEST_PRECEDENCE)
@Component
public class IDToMDCFilterBean extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(IDToMDCFilterBean.class);

    private final Oppslag oppslag;
    private final TokenUtil helper;

    public IDToMDCFilterBean(TokenUtil helper, Oppslag oppslag) {
        this.helper = helper;
        this.oppslag = oppslag;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (helper.erAutentisert()) {
            copyHeadersToMDC();
        }
        chain.doFilter(req, res);
    }

    private void copyHeadersToMDC() {
        try {
            String fnr = helper.getSubject();
            if (isDevOrPreprod(getEnvironment())) {
                MDC.put(NAV_USER_ID, fnr);
            }
            MDC.put(NAV_AKTØR_ID, oppslag.hentAktørId(fnr).getAktør());
        } catch (Exception e) {
            LOG.info("Noe gikk feil. ikke kritisk, men MDC-verdier er inkomplette", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + ", helper=" + helper + "]";
    }
}
