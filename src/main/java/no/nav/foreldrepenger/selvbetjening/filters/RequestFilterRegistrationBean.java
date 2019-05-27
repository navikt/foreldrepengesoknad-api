package no.nav.foreldrepenger.selvbetjening.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class RequestFilterRegistrationBean extends FilterRegistrationBean<RequestFilter> {
    private static final Logger LOG = LoggerFactory.getLogger(IDFilterRegistrationBean.class);

    public RequestFilterRegistrationBean(RequestFilter requestFilter) {
        setFilter(requestFilter);
        addUrlPatterns("/rest/soknad/*");
        addUrlPatterns("/rest/storage/*");
        addUrlPatterns("/rest/innsyn/*");
        LOG.info("Registrert filter {}", this);
    }
}
