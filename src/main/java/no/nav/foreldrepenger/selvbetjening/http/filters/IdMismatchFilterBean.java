package no.nav.foreldrepenger.selvbetjening.http.filters;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.FNR_HEADER_VALUE;

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

import no.nav.foreldrepenger.selvbetjening.error.IdMismatchException;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Component
@Order(1)
public class IdMismatchFilterBean extends GenericFilterBean {

    private final TokenUtil tokenUtil;
    private static final Logger LOG = LoggerFactory.getLogger(IdMismatchFilterBean.class);

    public IdMismatchFilterBean(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        checkIds(request);
        chain.doFilter(request, response);
    }

    private void checkIds(ServletRequest request) {
        String fnr = HttpServletRequest.class.cast(request).getHeader(FNR_HEADER_VALUE);
        if ((fnr != null) && (tokenUtil.getSubject() != null) && !fnr.equals(tokenUtil.getSubject())) {
            LOG.warn("ID Mismatch mellom {} og {}", fnr, tokenUtil.getSubject());
            throw new IdMismatchException(fnr, tokenUtil.getSubject());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tokenUtil=" + tokenUtil + "]";
    }
}
