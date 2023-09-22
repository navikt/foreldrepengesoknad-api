package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;

@ProtectedRestController("/rest/dokument")
public class DokumentArkivController {
    private static final Logger LOG = LoggerFactory.getLogger(DokumentArkivController.class);
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

    @Deprecated
    @GetMapping(value = "/alle/v2", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArkivDokumentDto> hentDokumentoversiktenV2(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        return innsyn.alleDokumenterPåSak(saksnummer);
    }

    @GetMapping(value = "/alle", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArkivDokumentDto> hentDokumentoversikten(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        return innsyn.alleDokumenterPåSak(saksnummer);
    }

    @Deprecated
    @GetMapping(value = "/dokumentoversikt", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArkivDokument> hentDokumentoversikt() {
        LOG.info("Kall på /rest/dokument/dokumentoversikt tjeneste");
        return dokumentArkivTjeneste.hentDokumentoversikt();
    }
}
