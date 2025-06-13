package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record DokumentDto(String tittel,
                          @NotNull DokumentDto.DokumentKategori type,
                          String saksnummer,
                          String journalpostId,
                          String dokumentId,
                          @NotNull LocalDateTime mottatt) {
    public enum DokumentKategori {
        INNGÅENDE_DOKUMENT,
        UTGÅENDE_DOKUMENT
    }
}
