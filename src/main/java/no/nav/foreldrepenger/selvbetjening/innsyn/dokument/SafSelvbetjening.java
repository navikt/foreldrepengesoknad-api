package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import org.springframework.http.ResponseEntity;

import no.nav.safselvbetjening.Dokumentoversikt;
import no.nav.safselvbetjening.DokumentoversiktResponseProjection;
import no.nav.safselvbetjening.DokumentoversiktSelvbetjeningQueryRequest;

public interface SafSelvbetjening {
    ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId);

    Dokumentoversikt dokumentoversiktSelvbetjening(DokumentoversiktSelvbetjeningQueryRequest q, DokumentoversiktResponseProjection p);
}
