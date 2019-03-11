package no.nav.foreldrepenger.selvbetjening.filters;

import no.nav.foreldrepenger.selvbetjening.util.CallIdGenerator;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_CONSUMER_ID;
import static no.nav.foreldrepenger.selvbetjening.util.MDCUtil.toMDC;

@Component
@Order
public class HeadersToMDCFilterBean extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(HeadersToMDCFilterBean.class);

    private final CallIdGenerator generator;
    private final String applicationName;
    private final TokenUtil tokenUtil;

    @Inject
    public HeadersToMDCFilterBean(CallIdGenerator generator, TokenUtil tokenUtil,
            @Value("${spring.application.name}") String applicationName) {
        this.generator = generator;
        this.tokenUtil = tokenUtil;
        this.applicationName = applicationName;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String uri = ((HttpServletRequest) req).getRequestURI();
        putValues((HttpServletRequest) req, uri);
        chain.doFilter(req, response);

    }

    private void putValues(HttpServletRequest request, String uri) {
        try {
            toMDC(NAV_CONSUMER_ID, request.getHeader(NAV_CONSUMER_ID), applicationName);
            toMDC(NAV_CALL_ID, request.getHeader(NAV_CALL_ID), generator.create());
        } catch (Exception e) {
            LOG.warn("Noe gikk galt ved setting av MDC-verdier for request {}, MDC-verdier er inkomplette", uri, e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [generator=" + generator + ", applicationName=" + applicationName
                + ", tokenUtil=" + tokenUtil + "]";
    }

}
