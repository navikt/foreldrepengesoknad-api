package no.nav.foreldrepenger.selvbetjening.interceptors.server;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAV_AKTØR_ID;
import static no.nav.foreldrepenger.selvbetjening.util.MDCUtil.toMDC;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.Oppslag;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Component
@Order(HIGHEST_PRECEDENCE)
@DependsOn("oppslagTjeneste")
public class SubjectToMCDHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectToMCDHandlerInterceptor.class);

    private final Oppslag oppslag;
    private final TokenUtil tokenUtil;

    public SubjectToMCDHandlerInterceptor(TokenUtil tokenUtil, Oppslag oppslag) {
        this.tokenUtil = tokenUtil;
        this.oppslag = oppslag;
        LOG.info("Registrert interceptor {}", this);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            LOG.info("SubjectToMCDHandlerInterceptor {}", getClass().getSimpleName());
            if (tokenUtil.erAutentisert()) {
                LOG.info("Henter ID");
                toMDC(NAV_AKTØR_ID, oppslag.hentAktørId(tokenUtil.getSubject()).getAktør());
                LOG.info("Hentet ID OK");
            }
        } catch (Exception e) {
            LOG.warn("Noe gikk galt ved setting av MDC-verdier for request {}, MDC-verdier er inkomplette",
                    request.getRequestURI(), e);
        }
        LOG.info("Eksekvert interceptor {} OK", getClass().getSimpleName());
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [oppslag=" + oppslag + ", tokenUtil=" + tokenUtil + "]";
    }
}
