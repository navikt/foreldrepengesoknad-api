package no.nav.foreldrepenger.selvbetjening.rest;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentConversionException;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentTypeUnsupportedException;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentsTooLargeException;
import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;

@ControllerAdvice
public class APIControllerAdvice extends ResponseEntityExceptionHandler {

    private final Counter notFoundCounter = Metrics.counter("fpsoknad.api.person.notfound");
    private static final Logger LOG = LoggerFactory.getLogger(APIControllerAdvice.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientException(HttpClientErrorException e, WebRequest request) {
        if (e.getStatusCode() == NOT_FOUND) {
            notFoundCounter.increment();
            return logAndhandle(NOT_FOUND, e, request);
        }
        throw e;
    }

    @ExceptionHandler(AttachmentsTooLargeException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleTooLargeAttachmentsError(AttachmentsTooLargeException e, WebRequest req) {
        return logAndhandle(PAYLOAD_TOO_LARGE, e, req);
    }

    @ExceptionHandler(AttachmentTypeUnsupportedException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleUnsupportedAttachmentError(AttachmentTypeUnsupportedException e,
            WebRequest req) {
        return logAndhandle(UNPROCESSABLE_ENTITY, e, req);
    }

    @ExceptionHandler(AttachmentConversionException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleAttachmentConversionError(AttachmentConversionException e, WebRequest req) {
        return logAndhandle(INTERNAL_SERVER_ERROR, e, req);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndhandle(UNPROCESSABLE_ENTITY, e, req, validationErrors(e));
    }

    @ExceptionHandler({ OIDCUnauthorizedException.class })
    public ResponseEntity<Object> handleUnauthorized(OIDCUnauthorizedException e, WebRequest req) {
        return logAndhandle(UNAUTHORIZED, e, req);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception e, WebRequest req) {
        LOG.warn(e.getMessage(), e);
        return logAndhandle(INTERNAL_SERVER_ERROR, e, req);
    }

    private ResponseEntity<Object> logAndhandle(HttpStatus status, Exception e, WebRequest req) {
        return logAndhandle(status, e, req, Collections.singletonList(e.getMessage()));
    }

    private ResponseEntity<Object> logAndhandle(HttpStatus status, Exception e, WebRequest req, List<String> messages) {
        LOG.warn("{}", messages, e);
        return handleExceptionInternal(e, new ApiError(status, e, messages), new HttpHeaders(), status, req);
    }

    private List<String> validationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(this::errorMessage)
                .collect(toList());
    }

    private String errorMessage(FieldError error) {
        return error.getDefaultMessage() + " (" + error.getField() + ")";
    }
}
