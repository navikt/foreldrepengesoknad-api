package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;

@ProtectedRestController("/rest/dokument")
public class DokumentArkivController {
    private static final Logger LOG = LoggerFactory.getLogger(DokumentArkivController.class);
    private final Innsyn innsyn;

    @Autowired
    public DokumentArkivController(Innsyn innsyn) {
        this.innsyn = innsyn;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}")
    public ResponseEntity<byte[]> hentDokumentV2(@Valid @PathVariable("journalpostId") JournalpostId journalpostId,
                                                 @Valid @PathVariable("dokumentId") DokumentInfoId dokumentId) {
        try {
            var dokumentDto = innsyn.hentDokument(journalpostId, dokumentId);
            return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, dokumentDto.contentDisp())
                .header(CONTENT_TYPE, dokumentDto.contentType())
                .body(dokumentDto.innhold());
        } catch (HttpClientErrorException.NotFound | HttpClientErrorException.Forbidden e) {
            LOG.warn("Klarte ikke hente dokument med journalpostid {} og dokumentid {} pga {}", journalpostId.value(), dokumentId.value(), e.getStatusText());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/alle", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArkivDokumentDto> hentDokumentoversikten(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        return innsyn.alleDokumenterPåSak(saksnummer);
    }
}
