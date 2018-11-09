package no.nav.foreldrepenger.selvbetjening.felles.error;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.List;

import javax.ws.rs.BadRequestException;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentConversionException;
import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentTypeUnsupportedException;
import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentsTooLargeException;
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;
import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientException(HttpClientErrorException e, WebRequest request) {
        if (e.getStatusCode() == NOT_FOUND) {
            return warnAndHandle(NOT_FOUND, e, request, getRootCauseMessage(e));
        }
        throw e;
    }

    @ResponseBody
    @ExceptionHandler(AttachmentsTooLargeException.class)
    protected ResponseEntity<Object> handleTooLargeAttachmentsError(AttachmentsTooLargeException e, WebRequest req) {
        return warnAndHandle(PAYLOAD_TOO_LARGE, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(AttachmentTypeUnsupportedException.class)
    protected ResponseEntity<Object> handleUnsupportedAttachmentError(AttachmentTypeUnsupportedException e,
            WebRequest req) {
        return warnAndHandle(UNPROCESSABLE_ENTITY, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(AttachmentConversionException.class)
    protected ResponseEntity<Object> handleAttachmentConversionError(AttachmentConversionException e, WebRequest req) {
        return warnAndHandle(INTERNAL_SERVER_ERROR, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    public ResponseEntity<Object> handleMultipartError(MultipartException e, WebRequest req) {
        return warnAndHandle(PAYLOAD_TOO_LARGE, e, req, getRootCauseMessage(e));
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return warnAndHandle(UNPROCESSABLE_ENTITY, e, req, validationErrors(e));
    }

    @ResponseBody
    @ExceptionHandler(OIDCUnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedOIDC(OIDCUnauthorizedException e, WebRequest req) {
        return traceAndHandle(UNAUTHORIZED, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException e, WebRequest req) {
        return traceAndHandle(UNAUTHORIZED, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(OIDCTokenValidatorException.class)
    public ResponseEntity<Object> handleForbiddenOIDC(OIDCTokenValidatorException e, WebRequest req) {
        return warnAndHandle(FORBIDDEN, e, req, e.getExpiryDate() != null ? e.getExpiryDate().toString() : null,
                getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Object> handeForbidden(UnauthenticatedException e, WebRequest req) {
        return warnAndHandle(FORBIDDEN, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException e, WebRequest req) {
        return warnAndHandle(BAD_REQUEST, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> catchAll(Exception e, WebRequest req) {
        return warnAndHandle(INTERNAL_SERVER_ERROR, e, req, getRootCauseMessage(e));
    }

    private ResponseEntity<Object> traceAndHandle(HttpStatus status, Exception e, WebRequest req,
            String... messages) {
        return traceAndHandle(status, e, req, asList(messages));
    }

    private ResponseEntity<Object> traceAndHandle(HttpStatus status, Exception e, WebRequest req,
            List<String> messages) {
        LOG.trace("{}", messages, e);
        return handleExceptionInternal(e, new ApiError(status, e, messages), new HttpHeaders(), status, req);
    }

    private ResponseEntity<Object> warnAndHandle(HttpStatus status, Exception e, WebRequest req,
            String... messages) {
        return warnAndHandle(status, e, req, asList(messages));
    }

    private ResponseEntity<Object> warnAndHandle(HttpStatus status, Exception e, WebRequest req,
            List<String> messages) {
        LOG.warn("{}", messages, e);
        return handleExceptionInternal(e, new ApiError(status, e, messages), new HttpHeaders(), status, req);
    }

    private List<String> validationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(this::errorMessage)
                .collect(toList());
    }

    private String getRootCauseMessage(Throwable t) {
        Throwable rootCause = getRootCause(t);
        return rootCause == null ? t.getMessage() : rootCause.getMessage();
    }

    private String errorMessage(FieldError error) {
        return error.getDefaultMessage() + " (" + error.getField() + ")";
    }
}
