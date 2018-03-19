package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = OIDCUnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorized(RuntimeException e, WebRequest request) {
        String body = e.getMessage();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Credentials", "true");
        headers.set("Access-Control-Allow-Origin", "http://localhost:8080");
        return handleExceptionInternal(e, body, headers, HttpStatus.UNAUTHORIZED, request);
    }

}
