package no.nav.foreldrepenger.selvbetjening;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.prometheus.client.hotspot.DefaultExports;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        DefaultExports.initialize();
        SpringApplication.run(Application.class, args);
    }
}
