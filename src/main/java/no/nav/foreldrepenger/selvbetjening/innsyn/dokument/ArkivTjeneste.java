package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.ArkivDokumentDto.Type.INNGÅENDE_DOKUMENT;
import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.ArkivDokumentDto.Type.UTGÅENDE_DOKUMENT;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;

@Service
public class ArkivTjeneste {

    private final SafSelvbetjeningTjeneste safSelvbetjening;

    public ArkivTjeneste(SafSelvbetjeningTjeneste safSelvbetjening) {
        this.safSelvbetjening = safSelvbetjening;
    }

    public ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return safSelvbetjening.hentDokument(journalpostId, dokumentId);
    }

    public List<ArkivDokumentDto> alle(Fødselsnummer fødselsnummer) {
        return tilArkivDokumenter(safSelvbetjening.alle(fødselsnummer));
    }

    public List<ArkivDokumentDto> alle(Fødselsnummer fnr, Saksnummer saksnummer) {
        return tilArkivDokumenter(safSelvbetjening.alle(fnr, saksnummer));
    }

    private List<ArkivDokumentDto> tilArkivDokumenter(List<EnkelJournalpost> journalposter) {
        return journalposter.stream()
            .flatMap(enkelJournalpost -> enkelJournalpost.dokumenter().stream()
                .map(dokument -> tilArkivdokument(dokument, enkelJournalpost))
            )
            .sorted(Comparator.comparing(ArkivDokumentDto::mottatt))
            .toList();
    }

    private static ArkivDokumentDto tilArkivdokument(EnkelJournalpost.Dokument dokument, EnkelJournalpost enkelJournalpost) {
        return new ArkivDokumentDto(
            dokument.tittel() != null ? dokument.tittel() : enkelJournalpost.tittel(),
            tilType(enkelJournalpost.type()),
            enkelJournalpost.saksnummer(),
            enkelJournalpost.journalpostId(),
            dokument.dokumentId(),
            enkelJournalpost.mottatt()
        );
    }

    private static ArkivDokumentDto.Type tilType(EnkelJournalpost.DokumentType type) {
        return switch (type) {
            case INNGÅENDE_DOKUMENT -> INNGÅENDE_DOKUMENT;
            case UTGÅENDE_DOKUMENT -> UTGÅENDE_DOKUMENT;
        };
    }
}
