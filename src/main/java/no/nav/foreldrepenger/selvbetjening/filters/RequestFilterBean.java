package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.FNR_HEADER_VALUE;
import static org.springframework.http.HttpStatus.CONFLICT;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Component
@Order(1)
public class RequestFilterBean extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(RequestFilterBean.class);

    private final TokenUtil tokenHelper;

    public RequestFilterBean(TokenUtil tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = HttpServletResponse.class.cast(response);

        try {
            String fnr = HttpServletRequest.class.cast(request).getHeader(FNR_HEADER_VALUE);
            if ((fnr != null) && (tokenHelper.getSubject() != null) && !fnr.equals(tokenHelper.getSubject())) {
                LOG.warn("FNR {} i header matcher ikke subject {} i token", fnr, tokenHelper.getSubject());
                res.sendError(CONFLICT.value());
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            LOG.warn("Feil i filter, ignorerer", e);
            chain.doFilter(request, response);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tokenHelper=" + tokenHelper + "]";
    }
}
