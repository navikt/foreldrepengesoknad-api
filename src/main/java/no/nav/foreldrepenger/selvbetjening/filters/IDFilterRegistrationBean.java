package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.filters.FilterRegistrationUtil.always;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class IDFilterRegistrationBean extends FilterRegistrationBean<IDToMDCFilterBean> {

    public IDFilterRegistrationBean(IDToMDCFilterBean idFilter) {
        setFilter(idFilter);
        setUrlPatterns(always());
    }
}
