package no.nav.foreldrepenger.selvbetjening.error;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import no.nav.foreldrepenger.selvbetjening.util.TokenHelper;
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;
import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Inject
    TokenHelper tokenHelper;

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException e, WebRequest req) {
        if (e.getStatusCode().equals(UNAUTHORIZED) || e.getStatusCode().equals(FORBIDDEN)) {
            if (tokenHelper.getExp() != null) {
                return handle(e.getStatusCode(), e, req, null, false, tokenHelper.getExp().toString());
            }
        }
        return handle(e.getStatusCode(), e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(AttachmentTypeUnsupportedException.class)
    protected ResponseEntity<Object> handleUnsupportedAttachmentError(AttachmentTypeUnsupportedException e,
            WebRequest req) {
        return handle(UNPROCESSABLE_ENTITY, e, req, getRootCauseMessage(e));
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return handle(UNPROCESSABLE_ENTITY, e, req, getRootCauseMessage(e), false, validationErrors(e));
    }

    @ResponseBody
    @ExceptionHandler(AttachmentConversionException.class)
    protected ResponseEntity<Object> handleAttachmentConversionError(AttachmentConversionException e, WebRequest req) {
        return handle(INTERNAL_SERVER_ERROR, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler({ MultipartException.class, MaxUploadSizeExceededException.class,
            AttachmentsTooLargeException.class })
    public ResponseEntity<Object> handleTooLargeAttchment(Exception e, WebRequest req) {
        return handle(PAYLOAD_TOO_LARGE, e, req, getRootCauseMessage(e));
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
            HttpStatus status, WebRequest req) {
        return handle(NOT_FOUND, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(OIDCUnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedOIDC(OIDCUnauthorizedException e, WebRequest req) {
        return handle(UNAUTHORIZED, e, req, getRootCauseMessage(e), true);
    }

    @ResponseBody
    @ExceptionHandler(OIDCTokenValidatorException.class)
    public ResponseEntity<Object> handleForbiddenOIDC(OIDCTokenValidatorException e, WebRequest req) {
        return handle(FORBIDDEN, e, req, getRootCauseMessage(e),
                e.getExpiryDate() != null ? e.getExpiryDate().toString() : null);
    }

    @ResponseBody
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Object> handleForbidden(UnauthenticatedException e, WebRequest req) {
        return handle(FORBIDDEN, e, req, getRootCauseMessage(e), false,
                e.getExpDate() != null ? e.getExpDate().toString() : null);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> catchAll(Exception e, WebRequest req) {
        return handle(INTERNAL_SERVER_ERROR, e, req, getRootCauseMessage(e));
    }

    private ResponseEntity<Object> handle(HttpStatus status, Exception e, WebRequest req, String msg, String... extra) {
        return handle(status, e, req, msg, false, extra);
    }

    private ResponseEntity<Object> handle(HttpStatus status, Exception e, WebRequest req, String msg, boolean trace,
            String... extra) {
        return handle(status, e, req, msg, trace, new ArrayList<>(asList(extra)));
    }

    private ResponseEntity<Object> handle(HttpStatus status, Exception e, WebRequest req, String msg,
            boolean trace, List<String> extra) {
        if (req instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) req;
            extra.add(servletRequest.getHttpMethod() + " " + servletRequest.getRequest().getRequestURI());
        }
        log(e, msg, trace, extra);
        return handleExceptionInternal(e, new ApiError(status, msg), new HttpHeaders(), status, req);
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

    private static void log(Exception e, String message, boolean trace, List<String> extraInfo) {
        if (trace) {
            LOG.trace("Error: {} - Extra info: {}", message, extraInfo, e);
        }
        else {
            LOG.warn("Error: {} - Extra info: {}", message, extraInfo, e);
        }
    }
}
