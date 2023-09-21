package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;

@ProtectedRestController("/rest/dokument")
public class DokumentArkivController {

    private final DokumentArkivTjeneste dokumentArkivTjeneste;
    private final SafselvbetjeningConnection safselvbetjeningConnection;
    private final Innsyn innsyn;

    @Autowired
    public DokumentArkivController(DokumentArkivTjeneste dokumentArkivTjeneste, SafselvbetjeningConnection safselvbetjeningConnection, Innsyn innsyn) {
        this.dokumentArkivTjeneste = dokumentArkivTjeneste;
        this.safselvbetjeningConnection = safselvbetjeningConnection;
        this.innsyn = innsyn;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}")
    public ResponseEntity<byte[]> hentDokumentV2(@Valid @PathVariable("journalpostId") JournalpostId journalpostId,
                                                 @Valid @PathVariable("dokumentId") DokumentInfoId dokumentId) {
        return safselvbetjeningConnection.hentDokument(journalpostId, dokumentId);
    }

    @GetMapping(value = "/alle/v2")
    public List<ArkivDokumentDto> hentDokumentoversikten(@Valid @NotNull @PathVariable("saksnummer") Saksnummer saksnummer) {
        return innsyn.alleDokumenterPåSak(saksnummer);
    }

    @Deprecated
    @GetMapping(value = "/alle", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArkivDokument> hentDokumentoversikten() {
        return dokumentArkivTjeneste.hentDokumentoversikt();
    }

    @Deprecated
    @GetMapping(value = "/dokumentoversikt", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArkivDokument> hentDokumentoversikt() {
        return dokumentArkivTjeneste.hentDokumentoversikt();
    }
}
