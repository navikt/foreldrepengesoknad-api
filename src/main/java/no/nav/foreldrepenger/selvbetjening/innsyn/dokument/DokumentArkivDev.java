package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;

@ConditionalOnNotProd
@ProtectedRestController("/rest/dokument")
public class DokumentArkivDev {

    private DokumentArkivTjeneste dokumentArkivTjeneste;

    @Inject
    public DokumentArkivDev(DokumentArkivTjeneste dokumentArkivTjeneste) {
        this.dokumentArkivTjeneste = dokumentArkivTjeneste;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] hentDokument(@PathVariable("journalpostId") String journalpostId, @PathVariable("dokumentId") String dokumentId) {
        return dokumentArkivTjeneste.hentDokument(journalpostId, dokumentId);
    }

    @GetMapping(value = "/alle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hentDokumentoversikt() {
        return ResponseEntity.ok().body(dokumentArkivTjeneste.hentDokumentoversikt());
    }
}
