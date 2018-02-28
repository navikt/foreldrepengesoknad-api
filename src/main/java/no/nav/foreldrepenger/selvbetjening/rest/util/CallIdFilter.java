package no.nav.foreldrepenger.selvbetjening.rest.util;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.selvbetjening.util.CallIdGenerator;

@Component
@Order(1)
public class CallIdFilter extends GenericFilterBean {

    private final CallIdGenerator generator;

    @Inject
    public CallIdFilter(CallIdGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String key = getOrCreateCallId(request);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(key);
        }
    }

    private String getOrCreateCallId(ServletRequest req) {
        String callId = HttpServletRequest.class.cast(req).getHeader(generator.getDefaultKey());
        return callId == null ? generator.generateCallId().getFirst() : generator.generateCallId(callId);
    }
}
