package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.selvbetjening.felles.util.FnrExtractor;
import no.nav.foreldrepenger.selvbetjening.felles.util.UUIDCallIdGenerator;
import no.nav.security.oidc.context.OIDCRequestContextHolder;

@Component
@Order(1)
public class CallIdAndUserIdFilter extends GenericFilterBean {

    private static final Logger LOG = getLogger(CallIdAndUserIdFilter.class);

    @Inject
    private OIDCRequestContextHolder contextHolder;

    private final UUIDCallIdGenerator generator;

    @Inject
    public CallIdAndUserIdFilter(UUIDCallIdGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        MDC.put("Nav-userId", FnrExtractor.extract(contextHolder));
        getOrCreateCallId(request);
        chain.doFilter(request, response);
    }

    private void getOrCreateCallId(ServletRequest req) {
        String callId = HttpServletRequest.class.cast(req).getHeader(generator.getCallIdKey());
        if (callId != null) {
            // LOG.trace("Callid is already set in request {}", callId);
            MDC.put(generator.getCallIdKey(), callId);
        }
        else {
            MDC.put(generator.getCallIdKey(), generator.create());
            // LOG.trace("Callid was not set in request, now set in MDC to {}",
            // MDC.get(generator.getCallIdKey()));
        }
    }
}
