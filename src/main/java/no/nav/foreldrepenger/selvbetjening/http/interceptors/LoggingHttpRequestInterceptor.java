package no.nav.foreldrepenger.selvbetjening.http.interceptors;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import no.nav.boot.conditionals.ConditionalOnNotProd;

@Order
@Component
@ConditionalOnNotProd
public class LoggingHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        LOG.info("Kaller {} med headers \n{}", request.getURI(), request.getHeaders());
        return execution.execute(request, body);
    }


    private static boolean hasError(HttpStatus code) {
        return code.series().equals(CLIENT_ERROR) || code.series().equals(SERVER_ERROR);
    }
}
