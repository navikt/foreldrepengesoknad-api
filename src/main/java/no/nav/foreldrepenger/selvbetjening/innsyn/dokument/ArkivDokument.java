package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.net.URI;
import java.time.LocalDateTime;

@Deprecated
public record ArkivDokument(DokumentType type,
                            LocalDateTime mottatt,
                            String saksnummer,
                            String tittel,
                            URI url,
                            String journalpost,
                            String dokumentId,
                            boolean erHoveddokument) {

    public enum DokumentType {
        UTGÅENDE_DOKUMENT,
        INNGÅENDE_DOKUMENT
    }

}
