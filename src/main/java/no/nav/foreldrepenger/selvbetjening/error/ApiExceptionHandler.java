package no.nav.foreldrepenger.selvbetjening.error;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.Optional;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.NestedServletException;

import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;

@ControllerAdvice
@ResponseBody
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final TokenUtil tokenUtil;

    public ApiExceptionHandler(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> handleNestedServletException(NestedServletException e, HttpHeaders headers,
            WebRequest req) {
        if (e.getCause() instanceof AttachmentException) {
            return handleAttachmentException(AttachmentException.class.cast(e.getCause()), req, headers);
        }
        return catchAll(e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers, validationErrors(e));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
            HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
            HttpStatus status, WebRequest req) {
        return logAndHandle(NOT_FOUND, e, req, headers);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleIncompleteException(UnexpectedInputException e, HttpHeaders headers,
            WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException e, WebRequest request,
            HttpHeaders headers) {
        return logAndHandle(e.getStatusCode(), e, request, headers);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleAttachmentException(AttachmentException e, WebRequest req,
            HttpHeaders headers) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers);
    }

    @ExceptionHandler({ MultipartException.class, MaxUploadSizeExceededException.class,
            AttachmentsTooLargeException.class, AttachmentTooLargeException.class })
    public ResponseEntity<Object> handleTooLargeAttchment(Exception e, WebRequest req, HttpHeaders headers) {
        return logAndHandle(PAYLOAD_TOO_LARGE, e, req, headers);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUnauthorizedJwt(JwtTokenUnauthorizedException e, WebRequest req,
            HttpHeaders headers) {
        return logAndHandle(UNAUTHORIZED, e, req, headers);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleForbiddenJwt(JwtTokenValidatorException e, WebRequest req,
            HttpHeaders headers) {
        return logAndHandle(FORBIDDEN, e, req, headers);
    }

    @ExceptionHandler
    public ResponseEntity<Object> catchAll(Exception e, WebRequest req, HttpHeaders headers) {
        return logAndHandle(INTERNAL_SERVER_ERROR, e, req, headers);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, HttpHeaders headers,
            Object... messages) {
        ApiError apiError = apiErrorFra(status, e, messages);
        LOG.warn("[{} ({})] {}  {} {} ({})", req.getContextPath(), subject(), headers, status, apiError.getMessages(),
                status.value(), e);
        return handleExceptionInternal(e, apiError, headers, status, req);
    }

    private Object[] validationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(ApiExceptionHandler::errorMessage)
                .toArray();
    }

    private String subject() {
        return Optional.ofNullable(tokenUtil.getSubject())
                .orElse("Uautentisert");
    }

    private static String errorMessage(FieldError error) {
        return error.getDefaultMessage() + " (" + error.getField() + ")";
    }

    private static ApiError apiErrorFra(HttpStatus status, Exception e, Object... messages) {
        return new ApiError(status, e, messages);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tokenUtil=" + tokenUtil + "]";
    }
}
