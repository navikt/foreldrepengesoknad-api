package no.nav.foreldrepenger.selvbetjening.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.vedlegg.UserfacingErrormessage;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static no.nav.foreldrepenger.common.util.MDCUtil.callId;
import static org.springframework.core.NestedExceptionUtils.getMostSpecificCause;

@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiError {

    private final HttpStatusCode status;
    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    @JsonFormat(with = WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
    private final List<String> messages;
    private final String uuid;
    private final String userfriendlyMessage;

    ApiError(HttpStatusCode status, Throwable t, MessageSource feilmeldingProvider, Object... objects) {
        this(status, t, null, feilmeldingProvider, objects);
    }

    ApiError(HttpStatusCode status, Throwable t, String destination,  MessageSource feilmeldingProvider, Object... objects) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.messages = messages(t, destination, objects);
        this.userfriendlyMessage = userfacingMessage(feilmeldingProvider, t);
        this.uuid = callId();
    }

    private String userfacingMessage(MessageSource feilmeldingProvider, Throwable t) {
        return t instanceof UserfacingErrormessage message ? message.getUserfacingErrorMessage(feilmeldingProvider) : null;
    }

    public String getUuid() {
        return uuid;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getUserfriendlyMessage() {
        return userfriendlyMessage;
    }

    private static List<String> messages(Throwable t, String destination, Object... objects) {
        var messages = Arrays.stream(objects).collect(Collectors.toList());
        var cause = getMostSpecificCause(t);
        if (!(cause instanceof MethodArgumentNotValidException)) {
            messages.add(cause.getMessage());
        }
        messages.add(destination);
        return messages.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[status=" + status + ", timestamp=" + timestamp + ", messages=" + messages
                + ", uuid=" + uuid + "]";
    }
}
