package no.nav.foreldrepenger.selvbetjening.filters;

import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static com.google.common.net.HttpHeaders.ORIGIN;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class CorsInterceptor extends HandlerInterceptorAdapter {

    private final List<String> allowedOrigins;

    @Inject
    public CorsInterceptor(@Value("${no.nav.foreldrepenger.api.allowed.origins:" +
            "https://engangsstonad.nais.oera-q.local," +
            "https://engangsstonad-t10.nav.no," +
            "https://engangsstonad-q.nav.no," +
            "https://engangsstonad.nav.no," +
            "https://foreldrepengesoknad.nais.oera-q.local, " +
            "https://foreldrepengesoknad-t10.nav.no," +
            "https://foreldrepengesoknad-q.nav.no," +
            "https://foreldrepengesoknad.nav.no," +
            "https://foreldrepengeoversikt.nais.oera-q.local," +
            "https://foreldrepenger-t10.nav.no," +
            "https://foreldrepenger-q.nav.no," +
            "https://foreldrepenger.nav.no}") String... allowedOrigins) {
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
        if (request.getMethod().equals(HttpMethod.POST)) {
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, "location");
        }
        if (request.getMethod().equals(HttpMethod.OPTIONS)) {
            response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE");
            response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type");
        }

        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [allowedOrigins=" + allowedOrigins + "]";
    }
}
