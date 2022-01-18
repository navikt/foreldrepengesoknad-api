package no.nav.foreldrepenger.selvbetjening.http.filters;

import static no.nav.foreldrepenger.common.util.Constants.NAV_TOKEN_EXPIRY_ID;
import static no.nav.foreldrepenger.common.util.MDCUtil.toMDC;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.selvbetjening.oppslag.Oppslag;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Order(HIGHEST_PRECEDENCE)
@Component
public class IDToMDCFilterBean extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(IDToMDCFilterBean.class);

    private final Oppslag oppslag;
    private final TokenUtil tokenUtil;

    public IDToMDCFilterBean(TokenUtil tokenUtil, Oppslag oppslag) {
        this.tokenUtil = tokenUtil;
        this.oppslag = oppslag;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String uri = HttpServletRequest.class.cast(req).getRequestURI();
        copyHeadersToMDC(uri);
        chain.doFilter(req, res);
    }

    private void copyHeadersToMDC(String uri) {
        try {
            if (tokenUtil.erAutentisert()) {
                toMDC(NAV_TOKEN_EXPIRY_ID, tokenUtil.getExpiryDate());
            }
        } catch (Exception e) {
            LOG.warn("Noe gikk galt ved setting av MDC-verdier for request {}, MDC-verdier er inkomplette", uri, e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + ", tokenUtil=" + tokenUtil + "]";
    }
}
