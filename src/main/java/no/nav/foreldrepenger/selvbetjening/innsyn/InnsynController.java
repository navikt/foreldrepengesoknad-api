package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.selvbetjening.innsyn.InnsynController.INNSYN;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;
import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentTjeneste;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;

@ProtectedRestController(INNSYN)
public class InnsynController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsynController.class);
    public static final String INNSYN = "/rest/innsyn/v2";
    private static final String TITTEL_VED_SØKNAD = "Søknad om";

    private final Innsyn innsynTjeneste;
    private final DokumentTjeneste dokumentTjeneste;
    private final TokenUtil tokenUtil;

    @Autowired
    public InnsynController(Innsyn innsyn, DokumentTjeneste dokumentTjeneste, TokenUtil tokenUtil) {
        this.innsynTjeneste = innsyn;
        this.dokumentTjeneste = dokumentTjeneste;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping("/saker")
    public Saker saker() {
        return innsynTjeneste.hentSaker();
    }

    @PostMapping(path = "/annenPartVedtak", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AnnenPartVedtak annenPartVedtak(@Valid @RequestBody AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return innsynTjeneste.annenPartVedtak(annenPartVedtakIdentifikator).orElse(null);
    }

    @GetMapping("/saker/oppdatert")
    public boolean erSakOppdatert() {
        var dokumenter = dokumentTjeneste.alle(tokenUtil.autentisertBrukerOrElseThrowException());
        var søkaderMottattNylig = dokumenter.stream()
            .filter(arkivDokument -> DokumentDto.Type.INNGÅENDE_DOKUMENT.equals(arkivDokument.type()))
            .filter(arkivDokument -> arkivDokument.tittel().contains(TITTEL_VED_SØKNAD))
            .filter(arkivDokument -> arkivDokument.mottatt().isAfter(LocalDateTime.now().minusDays(1)))
            .toList();

        if (søkaderMottattNylig.isEmpty()) {
            return true;
        }

        if (søkaderMottattNylig.stream().anyMatch(søknad -> søknad.saksnummer() == null)) {
            var førsteMottattDato = søkaderMottattNylig.stream()
                .min(Comparator.comparing(DokumentDto::mottatt))
                .map(DokumentDto::mottatt)
                .orElse(null);
            LOG.info("Sak ikke oppdatert. Fant søknad hvor saksnummer er null -> GOSYS. Antall {} Mottatt {}",
                søkaderMottattNylig.size(), førsteMottattDato);
            return false;
        }

        // Vi har nylig mottatt dokument. Sjekk at saken er oppdatert etter dette tidspunktet.
        var sakerFraFpoversikt = innsynTjeneste.hentSaker();
        var listeMedSakerFraFpoversikt = concat(sakerFraFpoversikt.foreldrepenger().stream(),
            concat(sakerFraFpoversikt.svangerskapspenger().stream(), sakerFraFpoversikt.engangsstønad().stream()))
            .toList();

        for (var søknad : søkaderMottattNylig) {
            var erSakOppdatert = listeMedSakerFraFpoversikt.stream()
                .filter(sak -> sak.saksnummer().value().equals(søknad.saksnummer()))
                .anyMatch(sak -> sak.oppdatertTidspunkt().isAfter(søknad.mottatt()));
            if (!erSakOppdatert) {
                LOG.info("Sak ikke oppdatert. Gjelder sak {} Mottatt {}", søknad.saksnummer(), søknad.mottatt());
                return false;
            }
        }
        return true;
    }
}

