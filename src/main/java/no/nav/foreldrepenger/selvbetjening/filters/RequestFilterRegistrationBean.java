package no.nav.foreldrepenger.selvbetjening.filters;

import static no.nav.foreldrepenger.selvbetjening.filters.FilterRegistrationUtil.urlPatternsFor;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingController.REST_SOKNAD;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.InnsynController.INNSYN;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringController.REST_STORAGE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class RequestFilterRegistrationBean extends FilterRegistrationBean<RequestFilterBean> {
    private static final Logger LOG = LoggerFactory.getLogger(RequestFilterRegistrationBean.class);

    public RequestFilterRegistrationBean(RequestFilterBean requestFilter) {
        setFilter(requestFilter);
        setUrlPatterns(urlPatternsFor(REST_SOKNAD, REST_STORAGE, INNSYN));
        LOG.info("Registrert filter {}", this);
    }
}
