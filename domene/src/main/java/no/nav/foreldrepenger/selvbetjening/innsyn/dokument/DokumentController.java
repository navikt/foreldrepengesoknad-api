package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ProtectedRestController("/rest/dokument")
public class DokumentController {
    private static final Logger LOG = LoggerFactory.getLogger(DokumentController.class);
    private final DokumentTjeneste dokumentTjeneste;
    private final Innsyn innsyn;
    private final TokenUtil tokenUtil;

    @Autowired
    public DokumentController(DokumentTjeneste dokumentTjeneste, Innsyn innsyn, TokenUtil tokenUtil) {
        this.dokumentTjeneste = dokumentTjeneste;
        this.innsyn = innsyn;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}")
    public ResponseEntity<byte[]> hentDokumentV2(@Valid @PathVariable("journalpostId") JournalpostId journalpostId,
                                                 @Valid @PathVariable("dokumentId") DokumentInfoId dokumentId) {
        try {
            var response = dokumentTjeneste.hentDokument(journalpostId, dokumentId);
            sammenlignDokument(response.getBody(), journalpostId, dokumentId);
            LOG.info("Hentet dokument med journalpostid {} og dokumentid {}", journalpostId, dokumentId);
            return response;
        } catch (HttpClientErrorException.NotFound | HttpClientErrorException.Forbidden e) {
            LOG.warn("Klarte ikke hente dokument med journalpostid {} og dokumentid {} pga {}", journalpostId.value(), dokumentId.value(),
                e.getStatusCode(), e);
            return ResponseEntity.notFound().build();
        }
    }

    private void sammenlignDokument(byte[] gammel, JournalpostId journalpostId, DokumentInfoId dokumentId) {
        try {
            var ny = innsyn.hentDokument(journalpostId, dokumentId);
            if (Arrays.equals(gammel, ny)) {
                LOG.info("DOKUMENT: Dokumentene er like");
            } else {
                LOG.info("DOKUMENT: Dokumentene er ulike!");
            }
        } catch (Exception e) {
            LOG.info("DOKUMENT: Sammenligning av dokument feilet", e);
        }
    }

    @GetMapping(value = "/alle")
    public List<DokumentDto> hentDokumentoversikten(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        var dokumenter = dokumentTjeneste.alle(tokenUtil.innloggetBrukerOrElseThrowException(), saksnummer);
        LOG.info("Hentet {} dokumenter på sak {}", dokumenter.size(), saksnummer.value());
        sammenlign(dokumenter, saksnummer);
        return dokumenter;
    }

    private void sammenlign(List<DokumentDto> gammel, Saksnummer saksnummer) {
        try {
            var ny = innsyn.dokumenter(saksnummer);
            if (erDokumentenLike(gammel, ny)) {
                LOG.info("DOKUMENTENE: Dokumentene er like");
            } else {
                LOG.info("DOKUMENTENE: Dokumentene er ulike! Gammel {}, ny {}", gammel, ny);
            }
        } catch (Exception e) {
            LOG.info("DOKUMENTENE: Sammenligning av dokumentene feilet", e);
        }
    }

    private boolean erDokumentenLike(List<DokumentDto> gammel, List<DokumentDto> ny) {
        if (Objects.equals(gammel, ny)) return true;
        if (gammel == null || ny == null) return false;
        if (gammel.size() != ny.size()) return false;

        return gammel.stream().allMatch(b ->
                Collections.frequency(gammel, b) == Collections.frequency(ny, b)
        );
    }
}
