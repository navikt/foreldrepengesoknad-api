package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.filters.FilterRegistrationUtil.urlPatternsFor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class HeadersToMDCFilterRegistrationBean extends FilterRegistrationBean<HeadersToMDCFilterBean> {
    private static final Logger LOG = LoggerFactory.getLogger(HeadersToMDCFilterRegistrationBean.class);

    public HeadersToMDCFilterRegistrationBean(HeadersToMDCFilterBean headersFilter) {
        setFilter(headersFilter);
        urlPatternsFor("/rest");
        LOG.info("Registrert filter {}", this);
    }
}
