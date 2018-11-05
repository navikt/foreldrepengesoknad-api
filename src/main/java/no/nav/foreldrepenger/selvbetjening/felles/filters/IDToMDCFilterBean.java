package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static no.nav.foreldrepenger.selvbetjening.felles.Constants.NAV_AKTØR_ID;
import static no.nav.foreldrepenger.selvbetjening.felles.Constants.NAV_USER_ID;
import static no.nav.foreldrepenger.selvbetjening.felles.util.EnvUtil.isDevOrPreprod;
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

import no.nav.foreldrepenger.selvbetjening.felles.util.TokenHandler;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.Oppslag;

@Order(HIGHEST_PRECEDENCE)
@Component
public class IDToMDCFilterBean extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(IDToMDCFilterBean.class);

    private final Oppslag oppslag;
    private final TokenHandler handler;

    public IDToMDCFilterBean(TokenHandler handler, Oppslag oppslag) {
        this.handler = handler;
        this.oppslag = oppslag;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (handler.erAutentisert()) {
            LOG.trace("Kopierer heder verdier hvis satt");
            copyHeadersToMDC();
        }
        else {
            LOG.trace("Ikke autentisert, ingenting å sette");
        }
        chain.doFilter(req, res);
    }

    private void copyHeadersToMDC() {
        try {
            if (isDevOrPreprod(getEnvironment())) {
                MDC.put(NAV_USER_ID, handler.getSubject());
            }
            MDC.put(NAV_AKTØR_ID, oppslag.hentAktørId(handler.getSubject()));
        } catch (Exception e) {
            LOG.warn("Noe gikk feil, MDC-verdier er inkomplette", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + ", handler=" + handler + "]";
    }
}
