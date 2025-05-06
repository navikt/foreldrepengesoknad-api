package no.nav.foreldrepenger.selvbetjening.innsyn;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.innsyn.AnnenPartSak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static no.nav.foreldrepenger.selvbetjening.innsyn.InnsynController.INNSYN;

@ProtectedRestController(INNSYN)
public class InnsynController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsynController.class);
    public static final String INNSYN = "/rest/innsyn/v2";

    private final Innsyn innsynTjeneste;

    @Autowired
    public InnsynController(Innsyn innsyn) {
        this.innsynTjeneste = innsyn;
    }

    @GetMapping("/saker")
    public Saker saker() {
        return innsynTjeneste.hentSaker();
    }

    @PostMapping(path = "/annenPartSak", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AnnenPartSak annenPartSak(@Valid @RequestBody AnnenPartSakIdentifikator annenPartSakIdentifikator) {
        return innsynTjeneste.annenPartSak(annenPartSakIdentifikator).orElse(null);
    }

    @PostMapping(path = "/annenPartVedtak", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AnnenPartSak annenPartVedtak(@Valid @RequestBody AnnenPartSakIdentifikator annenPartSakIdentifikator) {
        return innsynTjeneste.annenPartVedtak(annenPartSakIdentifikator).orElse(null);
    }

    @GetMapping("/saker/oppdatert")
    public boolean erSakOppdatert() {
        return innsynTjeneste.erOppdatert();
    }

    @PostMapping("/trengerDokumentereMorsArbeid")
    public boolean trengerDokumentereMorsArbeid(@Valid @RequestBody MorArbeidRequestDto morArbeidRequestDto) {
        try {
            return innsynTjeneste.trengerDokumentereMorsArbeid(morArbeidRequestDto);
        } catch (Exception e) {
            LOG.warn("Kall mot oversikt feilet ved sjekk om mors arbeid må doumenteres. Returnerer at det trengs dokumentasjon.", e);
            return true;
        }
    }
}

