package no.nav.foreldrepenger.selvbetjening.http.filters;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static no.nav.foreldrepenger.common.util.StringUtil.partialMask;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import no.nav.foreldrepenger.selvbetjening.error.IdMismatchException;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;

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
        if ((fnr != null) && tokenUtil.erInnloggetBruker() && !fnr.equals(tokenUtil.getSubject())) {
            LOG.warn("ID Mismatch. Fødselsnummer i søknad matcher ikke innlogget bruker. Forekommer typisk når søker og annenpart " +
                "søker i samme browser om hverandre. Enkelttilfeller kan ignoreres.");
            throw new IdMismatchException(partialMask(fnr), partialMask(tokenUtil.getSubject()));
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tokenUtil=" + tokenUtil + "]";
    }
}
