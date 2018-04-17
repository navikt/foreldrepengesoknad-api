package no.nav.foreldrepenger.selvbetjening.rest;

import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) class ApiError {

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