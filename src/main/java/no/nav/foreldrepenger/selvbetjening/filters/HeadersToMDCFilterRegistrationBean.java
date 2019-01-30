package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.filters.FilterRegistrationUtil.ALWAYS;
import static no.nav.foreldrepenger.selvbetjening.filters.FilterRegistrationUtil.urlPatternsFor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class HeadersToMDCFilterRegistrationBean extends FilterRegistrationBean<HeadersToMDCFilterBean> {

    public HeadersToMDCFilterRegistrationBean(HeadersToMDCFilterBean headersFilter) {
        setFilter(headersFilter);
        urlPatternsFor("/rest" + ALWAYS);
    }
}
