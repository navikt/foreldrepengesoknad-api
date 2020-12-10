package no.nav.foreldrepenger.selvbetjening.error;

import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.maskFnr;
import static org.springframework.http.HttpStatus.CONFLICT;
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

import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.security.token.support.core.exceptions.JwtTokenInvalidClaimException;
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
    public ResponseEntity<Object> handleIdMismatch(IdMismatchException e, WebRequest req) {
        return logAndHandle(CONFLICT, e, req);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleIncompleteException(UnexpectedInputException e,
            WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException e, WebRequest request) {
        return logAndHandle(e.getStatusCode(), e, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleAttachmentException(AttachmentException e, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req);
    }

    @ExceptionHandler({ MultipartException.class, MaxUploadSizeExceededException.class,
            AttachmentTooLargeException.class, AttachmentsTooLargeException.class })
    public ResponseEntity<Object> handleTooLargeAttchment(Exception e, WebRequest req) {
        return logAndHandle(PAYLOAD_TOO_LARGE, e, req);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUnauthorizedJwt(JwtTokenUnauthorizedException e, WebRequest req) {

        return Optional.ofNullable(e.getCause())
                .filter(JwtTokenInvalidClaimException.class::isInstance)
                .map(JwtTokenInvalidClaimException.class::cast)
                .map(i -> logAndHandle(FORBIDDEN, i, req))
                .orElse(logAndHandle(UNAUTHORIZED, e, req));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleForbiddenJwt(JwtTokenValidatorException e, WebRequest req) {
        e.getMessage();
        logAndHandle(FORBIDDEN, e, req, new HttpHeaders(), "");
        return logAndHandle(FORBIDDEN, e, req);
    }

    @ExceptionHandler
    public ResponseEntity<Object> catchAll(Exception e, WebRequest req) {
        LOG.warn("Ikke-forventet exception", e);
        return logAndHandle(INTERNAL_SERVER_ERROR, e, req);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req) {
        return logAndHandle(status, e, req, new HttpHeaders());
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, HttpHeaders headers,
            Object... messages) {
        ApiError apiError = apiErrorFra(status, e, messages);
        LOG.warn("[{} ({})] {} {} ({})", req.getContextPath(), subject(), status, apiError.getMessages(),
                status.value(), e);
        return handleExceptionInternal(e, apiError, headers, status, req);
    }

    private static Object[] validationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors()
                .stream()
                .map(ApiExceptionHandler::errorMessage)
                .toArray();
    }

    private String subject() {
        return Optional.ofNullable(maskFnr(tokenUtil.getSubject()))
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
