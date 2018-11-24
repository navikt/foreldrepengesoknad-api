package no.nav.foreldrepenger.selvbetjening.felles.filters;

import static no.nav.foreldrepenger.selvbetjening.felles.filters.FilterRegistrationUtil.urlPatternsFor;
import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagController.OPPSLAG;
import static no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynController.INNSYN;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class IDFilterRegistrationBean extends FilterRegistrationBean<IDToMDCFilterBean> {

    public IDFilterRegistrationBean(IDToMDCFilterBean idFilter) {
        setFilter(idFilter);
        setUrlPatterns(urlPatternsFor(OPPSLAG, INNSYN));
    }
}
