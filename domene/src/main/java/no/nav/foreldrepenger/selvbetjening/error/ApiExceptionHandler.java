package no.nav.foreldrepenger.selvbetjening.error;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static org.springframework.core.NestedExceptionUtils.getMostSpecificCause;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.io.EOFException;
import java.util.Optional;
import java.util.Set;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import no.nav.foreldrepenger.common.error.UnexpectedInputException;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.innsyn.UmydigBrukerException;
import no.nav.foreldrepenger.selvbetjening.uttak.ManglendeFamiliehendelseException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentPasswordProtectedException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentTooLargeException;
import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentsTooLargeException;
import no.nav.security.token.support.core.exceptions.JwtTokenInvalidClaimException;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;

import org.springframework.web.util.ContentCachingRequestWrapper;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");
    public static final String REDIRECT_INNLOGGING_VED_MANGLEDE_NIVÅ_ACR = "Required claims not present in token.[acr=idporten-loa-high]";
    private final TokenUtil tokenUtil;
    private final MessageSource messageSource;


    public ApiExceptionHandler(TokenUtil tokenUtil, MessageSource messageSource) {
        this.tokenUtil = tokenUtil;
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest req) {
        bodyToSecurelogs(req);
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException e, HttpHeaders headers, HttpStatusCode status, WebRequest req) {
        return logAndHandle(NOT_ACCEPTABLE, e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers, HttpStatusCode status, WebRequest req) {
        return logAndHandle(NOT_FOUND, e, req, headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest req) {
        var feltMedValideringsFeil = safeStream(e.getBindingResult().getFieldErrors())
            .map(ApiExceptionHandler::errorMessage)
            .toList();
        return logAndHandle(BAD_REQUEST, e, req, headers, feltMedValideringsFeil);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest req) {
        return logAndHandle(BAD_REQUEST, e, req);
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
    public ResponseEntity<Object> handleAttachmentException(AttachmentException e, WebRequest req) {
        return logAndHandle(UNPROCESSABLE_ENTITY, e, req);
    }

    @ExceptionHandler({ AttachmentTooLargeException.class, AttachmentsTooLargeException.class })
    public ResponseEntity<Object> handleTooLargeAttchment(Exception e, WebRequest req) {
        return logAndHandle(PAYLOAD_TOO_LARGE, e, req);
    }

    @ExceptionHandler({ MultipartException.class })
    public ResponseEntity<Object> handleMultipartException(Exception e, WebRequest req) {
        final var networkIssues = Set.of(IOFileUploadException.class, ClientAbortException.class, EOFException.class);
        var mostSpecificCause = getMostSpecificCause(e);
        if (networkIssues.contains(mostSpecificCause.getClass())) {
            return catchAll(e, req);
        } else if (mostSpecificCause instanceof MaxUploadSizeExceededException) {
            return handleTooLargeAttchment(e, req);
        } else {
            return logAndHandle(UNPROCESSABLE_ENTITY, e, req);
        }
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
        return logAndHandle(INTERNAL_SERVER_ERROR, e, req);
    }

    private ResponseEntity<Object> logAndHandle(HttpStatusCode status, Exception e, WebRequest req) {
        return logAndHandle(status, e, req, new HttpHeaders());
    }

    private void bodyToSecurelogs(WebRequest req) {
        try {
            var servletRequest = ((ServletWebRequest) req).getRequest();
            if (servletRequest instanceof ContentCachingRequestWrapper wrapper) {
                byte[] content = wrapper.getContentAsByteArray();
                if (content.length > 0) {
                    var body = new String(content, servletRequest.getCharacterEncoding());
                    SECURE_LOGGER.info("[{}] {}", fullPathTilKaltEndepunkt(req), body);
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private ResponseEntity<Object> logAndHandle(HttpStatusCode status, Exception e, WebRequest req, HttpHeaders headers, Object... messages) {
        if (erClientAbortException(e)) {
            var path = fullPathTilKaltEndepunkt(req);
            LOG.info("[{}] {}", path, "Client aborted request!", e);
            return null;
        }

        var apiError = apiErrorFra(status, e, messages);
        logException(status, e, req, apiError);
        return handleExceptionInternal(e, apiError, headers, status, req);
    }

    private void logException(HttpStatusCode status, Exception e, WebRequest req, ApiError apiError) {
        var path = fullPathTilKaltEndepunkt(req);

        if (loggExceptionPåInfoNivå(e)) {
            LOG.info("[{}] {} {}", path, status, apiError.getMessages(), e);
        } else {
            if (ikkeLoggExceptionsMedSensitiveOpplysnignerTilVanligLogg(e)) {
                LOG.warn("[{} ({})] {}", path, status, apiError.getMessages());
                SECURE_LOGGER.warn("[{}] {} {}", req.getContextPath(), status, apiError.getMessages(), e);
            } else {
                LOG.warn("[{}] {} {}", path, status, apiError.getMessages(), e);
            }
        }
    }

    private static boolean erClientAbortException(Exception e) {
        if (e instanceof ClientAbortException) {
            return true;
        }

        for (var cause = e.getCause(); cause != null; cause = cause.getCause()) {
            if (cause instanceof ClientAbortException) {
                return true;
            }
        }

        return false;
    }

    private boolean loggExceptionPåInfoNivå(Exception e) {
        return !tokenUtil.erInnloggetBruker() || tokenUtil.erUtløpt() ||
                e instanceof JwtTokenInvalidClaimException && getMostSpecificCause(e).getMessage().contains(REDIRECT_INNLOGGING_VED_MANGLEDE_NIVÅ_ACR) ||
                e instanceof JwtTokenUnauthorizedException && getMostSpecificCause(e).getMessage().contains(REDIRECT_INNLOGGING_VED_MANGLEDE_NIVÅ_ACR) ||
                e instanceof HttpMediaTypeNotAcceptableException ||
                e instanceof AttachmentPasswordProtectedException ||
                e instanceof UmydigBrukerException;
    }

    private boolean ikkeLoggExceptionsMedSensitiveOpplysnignerTilVanligLogg(Exception e) {
        return e instanceof MethodArgumentNotValidException || e instanceof ConstraintViolationException;
    }

    private static String errorMessage(FieldError error) {
        return "(" + error.getField() + ") " + error.getDefaultMessage();
    }

    private ApiError apiErrorFra(HttpStatusCode status, Exception e, Object... messages) {
        return new ApiError(status, e, messageSource, messages);
    }

    private static String fullPathTilKaltEndepunkt(WebRequest req) {
        try {
            return ((ServletWebRequest)req).getRequest().getRequestURI();
        } catch (Exception e) {
            return req.getContextPath();
        }
    }
}
