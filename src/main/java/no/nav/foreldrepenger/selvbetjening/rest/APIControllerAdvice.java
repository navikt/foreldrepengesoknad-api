package no.nav.foreldrepenger.selvbetjening.rest;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

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

@ControllerAdvice
public class APIControllerAdvice extends ResponseEntityExceptionHandler {

    private final Counter notFoundCounter = Metrics.counter("fpsoknad.api.person.notfound");
    private static final Logger LOG = LoggerFactory.getLogger(APIControllerAdvice.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientException(HttpClientErrorException e, WebRequest request) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            notFoundCounter.increment();
            LOG.warn("Got 404, is the gateway down?");
            return handleExceptionInternal(e, new ApiError(NOT_FOUND, e.getMessage(), e),
                    new HttpHeaders(),
                    NOT_FOUND,
                    request);
        }
        throw e;
    }

    @ExceptionHandler(AttachmentsTooLargeException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleTooLargeAttachmentsError(AttachmentsTooLargeException e,
            WebRequest request) {
        return handleExceptionInternal(e, new ApiError(PAYLOAD_TOO_LARGE, e.getMessage(), e),
                new HttpHeaders(),
                PAYLOAD_TOO_LARGE,
                request);
    }

    @ExceptionHandler(AttachmentTypeUnsupportedException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleUnsupportedAttachmentError(AttachmentTypeUnsupportedException e,
            WebRequest request) {
        return handleExceptionInternal(e, new ApiError(UNPROCESSABLE_ENTITY, e.getMessage(), e),
                new HttpHeaders(),
                UNPROCESSABLE_ENTITY,
                request);
    }

    @ExceptionHandler(AttachmentConversionException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleAttachmentConversionError(AttachmentConversionException e,
            WebRequest request) {
        return handleExceptionInternal(e, new ApiError(INTERNAL_SERVER_ERROR, e.getMessage(), e),
                new HttpHeaders(),
                INTERNAL_SERVER_ERROR,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(e, new ApiError(UNPROCESSABLE_ENTITY, validationResponseBody(e), e),
                new HttpHeaders(), UNPROCESSABLE_ENTITY, request);
    }

    private String validationResponseBody(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(this::errorMessage)
                .collect(joining("\n"));
    }

    private String errorMessage(FieldError error) {
        return error.getDefaultMessage() + " (" + error.getField() + ")";
    }

}
