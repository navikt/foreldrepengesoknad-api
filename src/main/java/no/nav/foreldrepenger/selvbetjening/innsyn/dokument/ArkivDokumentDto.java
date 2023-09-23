package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.time.LocalDateTime;

public record ArkivDokumentDto(String tittel,
                               Type type,
                               String saksnummer,
                               String journalpostId,
                               String dokumentId,
                               LocalDateTime mottatt) {
    public enum Type {
        INNGÅENDE_DOKUMENT,
        UTGÅENDE_DOKUMENT
    }
}
