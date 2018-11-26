package no.nav.foreldrepenger.selvbetjening.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.time.LocalDateTime.now;

@JsonInclude(NON_NULL)
class ApiError {

    private static final String UUID = "Nav-CallId";

    private final HttpStatus status;
    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private final String message;
    private final String uuid;

    ApiError(HttpStatus status, String message) {
        this.timestamp = now();
        this.status = status;
        this.message = message;
        this.uuid = uuid();
    }

    private static String uuid() {
        String uuid = MDC.get(UUID);
        return uuid != null ? uuid : MDC.get("X-" + UUID);
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