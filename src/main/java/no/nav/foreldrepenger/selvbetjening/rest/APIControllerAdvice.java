package no.nav.foreldrepenger.selvbetjening.rest;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentConversionException;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentTypeUnsupportedException;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentsTooLargeException;

@ControllerAdvice
public class APIControllerAdvice extends ResponseEntityExceptionHandler {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ApiError {

        private final HttpStatus status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private final LocalDateTime timestamp;
        private final String message;
        private final String uuid;

        ApiError(HttpStatus status) {
            this(status, null);
        }

        ApiError(HttpStatus status, Throwable ex) {
            this(status, "Unexpected error", ex);
        }

        ApiError(HttpStatus status, String message, Throwable ex) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.message = message;
            this.uuid = MDC.get("X-Nav-CallId");
        }

        public String getUuid() {
            return uuid;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getMessage() {
            return message;
        }

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
