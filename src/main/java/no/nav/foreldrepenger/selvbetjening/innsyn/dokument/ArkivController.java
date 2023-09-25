package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;

@ProtectedRestController("/rest/dokument")
public class ArkivController {
    private static final Logger LOG = LoggerFactory.getLogger(ArkivController.class);
    private final ArkivTjeneste arkivTjeneste;
    private final Innsyn innsyn;
    private final TokenUtil tokenUtil;

    public ArkivController(ArkivTjeneste arkivTjeneste, Innsyn innsyn, TokenUtil tokenUtil) {
        this.arkivTjeneste = arkivTjeneste;
        this.innsyn = innsyn;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping(value = "/hent-dokument/{journalpostId}/{dokumentId}")
    public ResponseEntity<byte[]> hentDokumentV2(@Valid @PathVariable("journalpostId") JournalpostId journalpostId,
                                                 @Valid @PathVariable("dokumentId") DokumentInfoId dokumentId) {
        try {
            var response = arkivTjeneste.hentDokument(journalpostId, dokumentId);
            LOG.info("Hentet dokument med journalpostid {} og dokumentid {}", journalpostId, dokumentId);
            return response;
        } catch (HttpClientErrorException.NotFound | HttpClientErrorException.Forbidden e) {
            LOG.warn("Klarte ikke hente dokument med journalpostid {} og dokumentid {} pga {}", journalpostId.value(), dokumentId.value(), e.getStatusText());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/alle")
    public List<ArkivDokumentDto> hentDokumentoversikten(@RequestParam @Valid @NotNull Saksnummer saksnummer) {
        var oversikt = innsyn.alleDokumenterPåSak(saksnummer);
        sammenlign(oversikt, saksnummer);
        LOG.info("Hentet {} dokumenter på sak {}", oversikt.size(), saksnummer.value());
        return oversikt;
    }

    private void sammenlign(List<ArkivDokumentDto> oversikt, Saksnummer saksnummer) {
        try {
            var direkte = arkivTjeneste.alle(tokenUtil.autentisertBrukerOrElseThrowException(), saksnummer);
            if (direkte.equals(oversikt)) {
                LOG.info("Ingen avvik i alle dokumenter");
            } else {
                LOG.info("AVVIK dokumenter: Oversikt {}, Direkte {}", oversikt, direkte);
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med sammenligning av dokumenter", e);
        }
    }
}
