package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static no.nav.foreldrepenger.selvbetjening.felles.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.selvbetjening.felles.Constants.NAV_CONSUMER_ID;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import no.nav.foreldrepenger.selvbetjening.felles.util.CallIdGenerator;

@Component
@Order(PriorityOrdered.HIGHEST_PRECEDENCE)
public class HeadersToMDCFilterBean extends GenericFilterBean {

    private final CallIdGenerator generator;
    private final String applicationName;

    @Inject
    public HeadersToMDCFilterBean(CallIdGenerator generator,
            @Value("${spring.application.name}") String applicationName) {
        this.generator = generator;
        this.applicationName = applicationName;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        putValues(HttpServletRequest.class.cast(request));
        chain.doFilter(request, response);
    }

    private void putValues(HttpServletRequest request) {
        putValue(NAV_CONSUMER_ID, request.getHeader(NAV_CONSUMER_ID), applicationName);
        putValue(NAV_CALL_ID, request.getHeader(NAV_CALL_ID), generator.create());
    }

    private static void putValue(String key, String value, String defaultValue) {
        MDC.put(key, Optional.ofNullable(value).orElse(defaultValue));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [generator=" + generator + ", applicationName=" + applicationName + "]";
    }

}
