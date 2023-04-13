package no.nav.foreldrepenger.selvbetjening.http.filters;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import jakarta.servlet.http.HttpServletRequest;

import static no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController.INNSENDING_CONTROLLER_PATH;

@Component
@ConditionalOnNotProd
public class BodyLoggingFilter extends AbstractRequestLoggingFilter {

    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");

    public BodyLoggingFilter() {
        super.setIncludeClientInfo(false);
        super.setIncludeQueryString(true);
        super.setIncludePayload(true);
        super.setAfterMessagePrefix("[");
        super.setAfterMessageSuffix("]");
        super.setMaxPayloadLength(100000);
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return !request.getMethod().equals("GET")
            && request.getRequestURI().contains(INNSENDING_CONTROLLER_PATH);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        // body ikke tilgjengelig
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        SECURE_LOGGER.info(message);
    }
}
