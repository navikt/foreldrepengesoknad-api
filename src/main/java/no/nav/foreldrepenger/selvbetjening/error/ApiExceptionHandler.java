package no.nav.foreldrepenger.selvbetjening.error;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;
import no.nav.security.spring.oidc.validation.interceptor.OIDCUnauthorizedException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Inject
    TokenUtil tokenHelper;

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> handleBadRequest(HttpMessageNotReadableException e, WebRequest request) {
        return logAndHandle(HttpStatus.BAD_REQUEST, e, request);
    }

    @ResponseBody
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException e, WebRequest request) {
        if (e.getStatusCode().equals(UNAUTHORIZED) || e.getStatusCode().equals(FORBIDDEN)) {
            return logAndHandle(e.getStatusCode(), e, request, tokenHelper.getExpiryDate());
        }
        return logAndHandle(e.getStatusCode(), e, request);
    }

    @ResponseBody
    @ExceptionHandler({ AttachmentTypeUnsupportedException.class, AttachmentConversionException.class })
    protected ResponseEntity<Object> handleAttachmentException(AttachmentException e, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, validationErrors(e));
    }

    @ResponseBody
    @ExceptionHandler({ MultipartException.class, MaxUploadSizeExceededException.class,
            AttachmentsTooLargeException.class })
    public ResponseEntity<Object> handleTooLargeAttchment(Exception e, WebRequest req) {
        return logAndHandle(PAYLOAD_TOO_LARGE, e, req);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
            HttpStatus status, WebRequest req) {
        return logAndHandle(NOT_FOUND, e, req);
    }

    @ResponseBody
    @ExceptionHandler(OIDCUnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedOIDC(OIDCUnauthorizedException e, WebRequest req) {
        return logAndHandle(UNAUTHORIZED, e, req);
    }

    @ResponseBody
    @ExceptionHandler(OIDCTokenValidatorException.class)
    public ResponseEntity<Object> handleForbiddenOIDC(OIDCTokenValidatorException e, WebRequest req) {
        return logAndHandle(FORBIDDEN, e, req, e.getExpiryDate());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> catchAll(Exception e, WebRequest req) {
        return logAndHandle(INTERNAL_SERVER_ERROR, e, req);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, Object... messages) {
        return logAndHandle(status, e, req, new HttpHeaders(), messages);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, HttpHeaders headers,
            Object... messages) {
        ApiError apiError = apiErrorFra(status, e, req, messages);
        LOG.warn("{} {} ({})", status, apiError.getMessages(), status.value(), e);
        return handleExceptionInternal(e, apiError, headers, status, req);
    }

    private Object[] validationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(ApiExceptionHandler::errorMessage)
                .toArray();
    }

    private static String errorMessage(FieldError error) {
        return error.getDefaultMessage() + " (" + error.getField() + ")";
    }

    private static ApiError apiErrorFra(HttpStatus status, Exception e, WebRequest req, Object... messages) {
        return req instanceof ServletWebRequest ? new ApiError(status, e, destFra(req), messages)
                : new ApiError(status, e, messages);
    }

    private static String destFra(WebRequest req) {
        return ServletWebRequest.class.cast(req).getRequest().getRequestURI();
    }
}
