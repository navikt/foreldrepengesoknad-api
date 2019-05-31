package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.FNR_HEADER_VALUE;

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

    private final TokenUtil tokenHelper;

    public RequestFilter(TokenUtil tokenHelper) {
        this.tokenHelper = tokenHelper;
    }

    private static final Logger LOG = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String fnr = req.getHeader(FNR_HEADER_VALUE);
        if (!tokenHelper.getSubject().equals(fnr)) {
            LOG.warn("FNR {} i header matcher ikke subject {} i token", fnr, tokenHelper.getSubject());
            res.sendError(409);
        }
        chain.doFilter(request, response);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tokenHelper=" + tokenHelper + "]";
    }
}