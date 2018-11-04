package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static com.google.common.collect.Lists.newArrayList;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class IDFilterRegistrationBean extends FilterRegistrationBean<IDToMDCFilterBean> {

    public IDFilterRegistrationBean(IDToMDCFilterBean idFilter) {
        setFilter(idFilter);
        setUrlPatterns(newArrayList("/rest/*"));
    }
}
