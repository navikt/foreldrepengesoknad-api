package no.nav.foreldrepenger.selvbetjening.filters;

import no.nav.foreldrepenger.selvbetjening.util.Enabled;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(1)
public class RequestFilter implements Filter {

    @Inject
    private TokenUtil tokenHelper;
    private static final Logger LOG = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (Enabled.FNR_HEADER_FILTER && req.getHeader("fnr") != null && !req.getHeader("fnr").equals(tokenHelper.getSubject())) {
            LOG.info("fnr i header matcher ikke subject i token");
            res.sendError(409);
        }

        chain.doFilter(request, response);
    }
}