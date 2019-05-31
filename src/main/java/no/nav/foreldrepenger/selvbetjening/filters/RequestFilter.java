package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.FNR_HEADER_VALUE;
import static org.springframework.http.HttpStatus.CONFLICT;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Component
@Order(1)
@ConditionalOnProperty(name = "TOGGLES_FNR_HEADER_FILTER", havingValue = "true", matchIfMissing = true)
public class RequestFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestFilter.class);

    private final TokenUtil tokenHelper;

    public RequestFilter(TokenUtil tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = HttpServletResponse.class.cast(response);

        String fnr = HttpServletRequest.class.cast(request).getHeader(FNR_HEADER_VALUE);
        if (!tokenHelper.getSubject().equals(fnr)) {
            LOG.warn("FNR {} i header matcher ikke subject {} i token", fnr, tokenHelper.getSubject());
            res.sendError(CONFLICT.value());
        }
        chain.doFilter(request, response);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tokenHelper=" + tokenHelper + "]";
    }
}