package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;

@ProtectedRestController("/rest/dokument")
public class DokumentController {
    private static final Logger LOG = LoggerFactory.getLogger(DokumentController.class);
    private final DokumentTjeneste dokumentTjeneste;
    private final TokenUtil tokenUtil;

    @Autowired
    public DokumentController(DokumentTjeneste dokumentTjeneste, TokenUtil tokenUtil) {
        this.dokumentTjeneste = dokumentTjeneste;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}")
    public ResponseEntity<byte[]> hentDokumentV2(@Valid @PathVariable("journalpostId") JournalpostId journalpostId,
                                                 @Valid @PathVariable("dokumentId") DokumentInfoId dokumentId) {
        try {
            var response = dokumentTjeneste.hentDokument(journalpostId, dokumentId);
            LOG.info("Hentet dokument med journalpostid {} og dokumentid {}", journalpostId, dokumentId);
            return response;
        } catch (HttpClientErrorException.NotFound | HttpClientErrorException.Forbidden e) {
            LOG.warn("Klarte ikke hente dokument med journalpostid {} og dokumentid {} pga {}", journalpostId.value(), dokumentId.value(),
                e.getStatusCode(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/alle")
    public List<DokumentDto> hentDokumentoversikten(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        var dokumenter = dokumentTjeneste.alle(tokenUtil.autentisertBrukerOrElseThrowException(), saksnummer);
        LOG.info("Hentet {} dokumenter på sak {}", dokumenter.size(), saksnummer.value());
        return dokumenter;
    }
}
