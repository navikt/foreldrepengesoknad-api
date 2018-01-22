package no.nav.foreldrepenger.selvbetjening;

import io.prometheus.client.hotspot.DefaultExports;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class ApplicationLocal {

    public static void main(String[] args) {
        DefaultExports.initialize();
        run(ApplicationLocal.class, args);
    }
}
