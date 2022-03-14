package no.nav.foreldrepenger.selvbetjening.http.filters;

import static no.nav.foreldrepenger.selvbetjening.http.filters.FilterRegistrationUtil.urlPatternsFor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class IDFilterRegistrationBean extends FilterRegistrationBean<IDToMDCFilterBean> {
    private static final Logger LOG = LoggerFactory.getLogger(IDFilterRegistrationBean.class);

    public IDFilterRegistrationBean(IDToMDCFilterBean idFilter) {
        setFilter(idFilter);
        setOrder(100);
        setUrlPatterns(urlPatternsFor("/rest"));
        LOG.trace("Registrert filter {}", this);
    }
}
