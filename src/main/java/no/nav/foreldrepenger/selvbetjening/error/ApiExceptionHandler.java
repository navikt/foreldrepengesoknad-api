package no.nav.foreldrepenger.selvbetjening.error;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.common.util.StringUtil.partialMask;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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

import no.nav.foreldrepenger.common.error.UnexpectedInputException;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.uttak.ManglendeFamiliehendelseException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentTooLargeException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentsTooLargeException;
import no.nav.security.token.support.core.exceptions.JwtTokenInvalidClaimException;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;

@ControllerAdvice
@ResponseBody
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");
    private final TokenUtil tokenUtil;

    public ApiExceptionHandler(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(NOT_ACCEPTABLE, e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        return logAndHandle(NOT_FOUND, e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest req) {
        var felterMedValideringsFeilOgTilhørendeAvvisteVerdi = safeStream(e.getBindingResult().getFieldErrors())
            .map(ApiExceptionHandler::errorMessageMedAvvisteVerdier)
            .toList();
        SECURE_LOGGER.warn("[{} ({})] Valideringsfeil: {}", req.getContextPath(), status, felterMedValideringsFeilOgTilhørendeAvvisteVerdi, e);
        var feltMedValideringsFeil = safeStream(e.getBindingResult().getFieldErrors())
            .map(ApiExceptionHandler::errorMessage)
            .toList();
        return logAndHandle(BAD_REQUEST, e, req, headers, feltMedValideringsFeil);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest req) {
        var felterMedValideringsFeilOgTilhørendeAvvisteVerdi = safeStream(e.getConstraintViolations())
            .map(ApiExceptionHandler::errorMessageMedAvvisteVerdier)
            .toList();
        SECURE_LOGGER.warn("[{} ({})] Valideringsfeil: {}", req.getContextPath(),  e.getLocalizedMessage(), felterMedValideringsFeilOgTilhørendeAvvisteVerdi, e);
        var feltMedValideringsFeil = safeStream(e.getConstraintViolations())
            .map(ApiExceptionHandler::errorMessage)
            .toList();
        return logAndHandle(BAD_REQUEST, e, req, feltMedValideringsFeil);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleIdMismatch(IdMismatchException e, WebRequest req) {
        return logAndHandle(CONFLICT, e, req);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleIncompleteException(UnexpectedInputException e, WebRequest req) {
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
        return logAndHandle(FORBIDDEN, e, req);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleManglendeFamiliehendelse(ManglendeFamiliehendelseException e, WebRequest req) {
        LOG.info("{} Mangler familiehendelse", req.getContextPath(), e);
        return new ResponseEntity<>("Mangler fødselsdato/termindato/omsorgsovertakelse", new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> catchAll(Exception e, WebRequest req) {
        LOG.warn("Ikke-forventet exception", e);
        return logAndHandle(INTERNAL_SERVER_ERROR, e, req);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req) {
        return logAndHandle(status, e, req, new HttpHeaders());
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, Object... messages) {
        return logAndHandle(status, e, req, new HttpHeaders(), messages);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatus status, Exception e, WebRequest req, HttpHeaders headers,
            Object... messages) {
        ApiError apiError = apiErrorFra(status, e, messages);
        if (e instanceof MethodArgumentNotValidException || e instanceof ConstraintViolationException) {
            LOG.warn("[{} ({})] {}", req.getContextPath(), status, apiError.getMessages());
        } else if (tokenUtil.erAutentisert() && !tokenUtil.erUtløpt()) {
            LOG.warn("[{} ({})] {} {} ({})", req.getContextPath(), subject(), status, apiError.getMessages(), status.value(), e);
        } else {
            LOG.debug("[{}] {} {} ({})", req.getContextPath(), status, apiError.getMessages(), status.value(), e);
        }
        return handleExceptionInternal(e, apiError, headers, status, req);
    }


    private String subject() {
        return Optional.ofNullable(partialMask(tokenUtil.getSubject()))
            .orElse("Uautentisert");
    }

    private static String errorMessage(FieldError error) {
        return error.getDefaultMessage() + " (" + error.getField() + ")";
    }

    private static String errorMessageMedAvvisteVerdier(FieldError error) {
        return String.format("%s (%s) %s. Avvist verdi er: %s",
            error.getObjectName(),
            error.getField(),
            error.getDefaultMessage(),
            error.getRejectedValue());
    }

    private static String errorMessage(ConstraintViolation<?> violation) {
        return String.format("%s (%s) %s",
            violation.getRootBeanClass().getName(),
            violation.getPropertyPath(),
            violation.getMessage());
    }

    private static String errorMessageMedAvvisteVerdier(ConstraintViolation<?> violation) {
        return String.format("%s (%s) %s. Avvist verdi er: %s",
            violation.getRootBeanClass().getName(),
            violation.getPropertyPath(),
            violation.getMessage(),
            violation.getInvalidValue());
    }

    private static ApiError apiErrorFra(HttpStatus status, Exception e, Object... messages) {
        return new ApiError(status, e, messages);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tokenUtil=" + tokenUtil + "]";
    }
}
