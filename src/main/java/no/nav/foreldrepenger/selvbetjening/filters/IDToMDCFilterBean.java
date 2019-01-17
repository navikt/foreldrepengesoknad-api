package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_AKTØR_ID;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_USER_ID;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.isDevOrPreprod;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

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
import no.nav.foreldrepenger.selvbetjening.util.TokenHelper;

@Order(HIGHEST_PRECEDENCE)
@Component
public class IDToMDCFilterBean extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(IDToMDCFilterBean.class);

    private final Oppslag oppslag;
    private final TokenHelper handler;

    public IDToMDCFilterBean(TokenHelper handler, Oppslag oppslag) {
        this.handler = handler;
        this.oppslag = oppslag;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (handler.erAutentisert()) {
            copyHeadersToMDC();
        }
        chain.doFilter(req, res);
    }

    private void copyHeadersToMDC() {
        try {
            String fnr = handler.getSubject();
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
        return getClass().getSimpleName() + " [oppslag=" + oppslag + ", handler=" + handler + "]";
    }
}
