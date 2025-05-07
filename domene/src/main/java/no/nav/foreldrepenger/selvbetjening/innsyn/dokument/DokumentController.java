package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;

@ProtectedRestController("/rest/dokument")
public class DokumentController {

    private final Innsyn innsyn;

    @Autowired
    public DokumentController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] hentDokumentV2(@Valid @PathVariable("journalpostId") JournalpostId journalpostId,
                                 @Valid @PathVariable("dokumentId") DokumentInfoId dokumentId) {
        return innsyn.hentDokument(journalpostId, dokumentId);
    }


    @GetMapping(value = "/alle")
    public List<DokumentDto> hentDokumentoversikten(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        return innsyn.dokumenter(saksnummer);
    }
}
