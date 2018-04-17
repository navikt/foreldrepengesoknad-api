package no.nav.foreldrepenger.selvbetjening.rest;

import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentsTooLargeException;

@ControllerAdvice
public class APIControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AttachmentsTooLargeException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleTooLargeAttachments(AttachmentsTooLargeException e, WebRequest request) {
        return handleExceptionInternal(e, null, new HttpHeaders(), PAYLOAD_TOO_LARGE, request);
    }

}
