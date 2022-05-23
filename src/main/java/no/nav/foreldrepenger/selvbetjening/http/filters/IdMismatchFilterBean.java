package no.nav.foreldrepenger.selvbetjening.http.filters;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static no.nav.foreldrepenger.common.util.StringUtil.partialMask;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.error.IdMismatchException;

@Component
public class IdMismatchFilterBean extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(IdMismatchFilterBean.class);
    private final TokenUtil tokenUtil;

    public IdMismatchFilterBean(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        checkIds(request);
        chain.doFilter(request, response);
    }

    private void checkIds(ServletRequest request) {
        var fnr = ((HttpServletRequest) request).getHeader(FNR);
        if ((fnr != null) && tokenUtil.erAutentisert() && !fnr.equals(tokenUtil.autentisertBruker().value())) {
            LOG.warn("ID Mismatch. Fødselsnummer i søknad matcher ikke innlogget bruker. Forekommer typisk når søker og annenpart " +
                "søker i samme browser om hverandre. Enkelttilfeller kan ignoreres.");
            throw new IdMismatchException(partialMask(fnr), partialMask(tokenUtil.autentisertBruker().value()));
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tokenUtil=" + tokenUtil + "]";
    }
}
