package no.nav.foreldrepenger.selvbetjening.rest.filters;

import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.google.common.net.HttpHeaders.ORIGIN;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class CorsInterceptor extends HandlerInterceptorAdapter {

    private final List<String> allowedOrigins;

    @Inject
    public CorsInterceptor(
            @Value("${no.nav.foreldrepenger.api.allowed.origins:http://localhost:8080,https://engangsstonad.nais.oera-q.local,https://engangsstonad-q.nav.no,https://engangsstonad.nav.no}") String... allowedOrigins) {
        this(Arrays.asList(allowedOrigins));
    }

    public CorsInterceptor(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String origin = request.getHeader(ORIGIN);
        if (allowedOrigins.contains(origin)) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [allowedOrigins=" + allowedOrigins + "]";
    }
}
