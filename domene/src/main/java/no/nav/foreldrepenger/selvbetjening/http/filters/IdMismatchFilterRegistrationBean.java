package no.nav.foreldrepenger.selvbetjening.http.filters;

import static no.nav.foreldrepenger.selvbetjening.http.filters.FilterRegistrationUtil.urlPatternsFor;
import static no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController.INNSENDING_CONTROLLER_PATH;
import static no.nav.foreldrepenger.selvbetjening.innsyn.InnsynController.INNSYN;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.MellomlagringController.REST_STORAGE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class IdMismatchFilterRegistrationBean extends FilterRegistrationBean<IdMismatchFilterBean> {
    private static final Logger LOG = LoggerFactory.getLogger(IdMismatchFilterRegistrationBean.class);

    public IdMismatchFilterRegistrationBean(IdMismatchFilterBean requestFilter) {
        setFilter(requestFilter);
        setOrder(10);
        setUrlPatterns(urlPatternsFor(INNSENDING_CONTROLLER_PATH, REST_STORAGE, INNSYN));
        LOG.trace("Registrert filter {}", this);
    }
}
