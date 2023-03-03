package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;
import javax.validation.Valid;

@ProtectedRestController("/rest/dokument")
public class DokumentArkivController {

    private final DokumentArkivTjeneste dokumentArkivTjeneste;

    @Inject
    public DokumentArkivController(DokumentArkivTjeneste dokumentArkivTjeneste) {
        this.dokumentArkivTjeneste = dokumentArkivTjeneste;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] hentDokument(@Valid @PathVariable("journalpostId") JournalpostId journalpostId,
                               @Valid @PathVariable("dokumentId") DokumentInfoId dokumentId) {
        return dokumentArkivTjeneste.hentDokument(journalpostId, dokumentId);
    }

    @GetMapping(value = "/alle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hentDokumentoversikten() {
        return ResponseEntity.ok().body(dokumentArkivTjeneste.hentDokumentoversikt());
    }

    @GetMapping(value = "/dokumentoversikt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hentDokumentoversikt() {
        return ResponseEntity.ok().body(dokumentArkivTjeneste.hentDokumentoversikt());
    }
}
