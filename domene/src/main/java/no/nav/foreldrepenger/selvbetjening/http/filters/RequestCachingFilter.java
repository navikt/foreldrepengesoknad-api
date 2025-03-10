package no.nav.foreldrepenger.selvbetjening.http.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;

import jakarta.servlet.ServletResponse;

import jakarta.servlet.http.HttpServletRequest;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

// filteret cacher body for senere logging ved 422
@Component
public class RequestCachingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        if (httpRequest.getRequestURI().contains(InnsendingController.INNSENDING_CONTROLLER_PATH)) {
            var cachedRequest = new ContentCachingRequestWrapper(httpRequest);
            chain.doFilter(cachedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
