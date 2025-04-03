package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DokumentDto(String tittel,
                          @NotNull Type type,
                          String saksnummer,
                          String journalpostId,
                          String dokumentId,
                          @NotNull LocalDateTime mottatt) {
    public enum Type {
        INNGÅENDE_DOKUMENT,
        UTGÅENDE_DOKUMENT
    }
}
