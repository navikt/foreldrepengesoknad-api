package no.nav.foreldrepenger.selvbetjening.http.filters;

import static no.nav.foreldrepenger.common.util.Constants.NAV_USER_ID;
import static no.nav.foreldrepenger.common.util.MDCUtil.toMDC;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.NAV_AUTH_LEVEL;
import static no.nav.foreldrepenger.selvbetjening.http.TokenUtil.NAV_TOKEN_EXPIRY_ID;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import no.nav.foreldrepenger.common.util.AuthenticationLevel;
import no.nav.foreldrepenger.common.util.StringUtil;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.oppslag.Oppslag;

@Component
public class IDToMDCFilterBean extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(IDToMDCFilterBean.class);
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");

    private final Oppslag oppslag;
    private final TokenUtil tokenUtil;

    public IDToMDCFilterBean(TokenUtil tokenUtil, Oppslag oppslag) {
        this.tokenUtil = tokenUtil;
        this.oppslag = oppslag;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) req;
        var uri = httpServletRequest.getRequestURI();
        copyHeadersToMDC(httpServletRequest, uri);
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }

    private void copyHeadersToMDC(HttpServletRequest request, String uri) {
        try {
            if (tokenUtil.erInnloggetBruker()) {
                var fnr = tokenUtil.getSubject();
                SECURE_LOGGER.info("FNR {} - {} {}", fnr, request.getMethod(), request.getRequestURI());
                toMDC(NAV_USER_ID, Optional.ofNullable(fnr).map(StringUtil::mask).orElse("Uautentisert"));
                toMDC(NAV_AUTH_LEVEL, Optional.ofNullable(tokenUtil.getLevel()).map(AuthenticationLevel::name).orElse(AuthenticationLevel.NONE.name()));
                toMDC(NAV_TOKEN_EXPIRY_ID, tokenUtil.getExpiration());
                toMDC("JTI", tokenUtil.getJti());
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
