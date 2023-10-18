package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentDto.Type.INNGÅENDE_DOKUMENT;
import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentDto.Type.UTGÅENDE_DOKUMENT;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;

@Service
public class DokumentTjeneste {

    private final SafSelvbetjeningTjeneste safSelvbetjening;

    public DokumentTjeneste(SafSelvbetjeningTjeneste safSelvbetjening) {
        this.safSelvbetjening = safSelvbetjening;
    }

    public ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return safSelvbetjening.hentDokument(journalpostId, dokumentId);
    }

    public List<DokumentDto> alle(Fødselsnummer fødselsnummer) {
        return tilArkivDokumenter(safSelvbetjening.alle(fødselsnummer));
    }

    public List<DokumentDto> alle(Fødselsnummer fnr, Saksnummer saksnummer) {
        return tilArkivDokumenter(safSelvbetjening.alle(fnr, saksnummer));
    }

    private List<DokumentDto> tilArkivDokumenter(List<EnkelJournalpost> journalposter) {
        return journalposter.stream()
            .flatMap(enkelJournalpost -> enkelJournalpost.dokumenter().stream()
                .map(dokument -> tilArkivdokument(dokument, enkelJournalpost))
            )
            .sorted(Comparator.comparing(DokumentDto::mottatt))
            .toList();
    }

    private static DokumentDto tilArkivdokument(EnkelJournalpost.Dokument dokument, EnkelJournalpost enkelJournalpost) {
        return new DokumentDto(
            dokument.tittel() != null ? dokument.tittel() : enkelJournalpost.tittel(),
            tilType(enkelJournalpost.type()),
            enkelJournalpost.saksnummer(),
            enkelJournalpost.journalpostId(),
            dokument.dokumentId(),
            enkelJournalpost.mottatt()
        );
    }

    private static DokumentDto.Type tilType(EnkelJournalpost.DokumentType type) {
        return switch (type) {
            case INNGÅENDE_DOKUMENT -> INNGÅENDE_DOKUMENT;
            case UTGÅENDE_DOKUMENT -> UTGÅENDE_DOKUMENT;
        };
    }
}
