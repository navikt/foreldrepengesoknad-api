package no.nav.foreldrepenger.selvbetjening.error;

import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;
import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientException(HttpClientErrorException e, WebRequest request) {
        if (e.getStatusCode() == NOT_FOUND) {
            return handleError(NOT_FOUND, e, request, getRootCauseMessage(e));
        }
        throw e;
    }

    @ResponseBody
    @ExceptionHandler(AttachmentsTooLargeException.class)
    protected ResponseEntity<Object> handleTooLargeAttachmentsError(AttachmentsTooLargeException e, WebRequest req) {
        return handleError(PAYLOAD_TOO_LARGE, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(AttachmentTypeUnsupportedException.class)
    protected ResponseEntity<Object> handleUnsupportedAttachmentError(AttachmentTypeUnsupportedException e, WebRequest req) {
        return handleError(UNPROCESSABLE_ENTITY, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(AttachmentConversionException.class)
    protected ResponseEntity<Object> handleAttachmentConversionError(AttachmentConversionException e, WebRequest req) {
        return handleError(INTERNAL_SERVER_ERROR, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleMultipartError(MultipartException e, WebRequest req) {
        return handleError(PAYLOAD_TOO_LARGE, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededError(MaxUploadSizeExceededException e, WebRequest req) {
        return handleError(PAYLOAD_TOO_LARGE, e, req, getRootCauseMessage(e));
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        return handleError(UNPROCESSABLE_ENTITY, e, req, getRootCauseMessage(e), false, validationErrors(e));
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        return handleError(NOT_FOUND, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(OIDCUnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedOIDC(OIDCUnauthorizedException e, WebRequest req) {
        return handleError(UNAUTHORIZED, e, req, getRootCauseMessage(e), true);
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException e, WebRequest req) {
        return handleError(UNAUTHORIZED, e, req, getRootCauseMessage(e), true);
    }

    @ResponseBody
    @ExceptionHandler(OIDCTokenValidatorException.class)
    public ResponseEntity<Object> handleForbiddenOIDC(OIDCTokenValidatorException e, WebRequest req) {
        return handleError(FORBIDDEN, e, req, getRootCauseMessage(e), e.getExpiryDate() != null ? e.getExpiryDate().toString() : null);
    }

    @ResponseBody
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Object> handeForbidden(UnauthenticatedException e, WebRequest req) {
        return handleError(FORBIDDEN, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException e, WebRequest req) {
        return handleError(BAD_REQUEST, e, req, getRootCauseMessage(e));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> catchAll(Exception e, WebRequest req) {
        return handleError(INTERNAL_SERVER_ERROR, e, req, getRootCauseMessage(e));
    }

    private ResponseEntity<Object> handleError(HttpStatus status, Exception e, WebRequest req, String message, String... extraInfo) {
        return handleError(status, e, req, message, false, extraInfo);
    }

    private ResponseEntity<Object> handleError(HttpStatus status, Exception e, WebRequest req, String message, boolean trace, String... extraInfo) {
        return handleError(status, e, req, message, trace, new ArrayList<>(asList(extraInfo)));
    }

    private ResponseEntity<Object> handleError(HttpStatus status, Exception e, WebRequest req, String message, boolean trace, List<String> extraInfo) {
        if (req instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) req;
            extraInfo.add(servletRequest.getRequest().getRequestURI());
        }

        if (trace) {
            LOG.trace("Error: {} - Extra info: {}", message, extraInfo, e);
        } else {
            LOG.warn("Error: {} - Extra info: {}", message, extraInfo, e);
        }

        return handleExceptionInternal(e, new ApiError(status, message), new HttpHeaders(), status, req);
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
