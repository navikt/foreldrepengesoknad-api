package no.nav.foreldrepenger.selvbetjening.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @GetMapping("/isAlive")
    public String isAlive() {
        return "Application: UP";
    }

    @GetMapping("/selftest")
    public String selftest() {
        return "Service status: OK";
    }

}
