package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record EnkelJournalpost(String tittel,
                               String journalpostId,
                               String saksnummer,
                               Bruker bruker,
                               DokumentType type,
                               LocalDateTime mottatt,
                               DokumentTypeId hovedtype,
                               List<Dokument> dokumenter) {
    public record Bruker(String id, Type type) {
        public enum Type {
            AKTOERID,
            FNR,
            ORGNR
        }

        @Override
        public String toString() {
            return "Bruker{" + "type=" + type + '}';
        }

    }

    public enum DokumentType {
        INNGÅENDE_DOKUMENT,
        UTGÅENDE_DOKUMENT
    }

    public record Dokument(String dokumentId, String tittel, Brevkode brevkode) {
    }

    public enum Brevkode {
        FORELDREPENGER_ANNULLERT("ANUFOR"),
        FORELDREPENGER_AVSLAG("AVSFOR"),
        SVANGERSKAPSPENGER_OPPHØR("OPPSVP"),
        ENGANGSSTØNAD_INNVILGELSE("INNVES"),
        SVANGERSKAPSPENGER_AVSLAG("AVSSVP"),
        FORELDREPENGER_INNVILGELSE("INVFOR"),
        ENGANGSSTØNAD_AVSLAG("AVSLES"),
        FORELDREPENGER_OPPHØR("OPPFOR"),
        SVANGERSKAPSPENGER_INNVILGELSE("INVSVP"),
        INNHENTE_OPPLYSNINGER("INNOPP"),
        ETTERLYS_INNTEKTSMELDING("ELYSIM"),
        FRITEKSTBREV("FRITEK"),

        // Gamle/utdaterte brevkoder funnet i Joark
        VEDTAK_POSITIVT_OLD("000048"),
        VEDTAK_AVSLAG_OLD("000051"),
        VEDTAK_FORELDREPENGER_OLD("000061"),
        VEDTAK_AVSLAG_FORELDREPENGER_OLD("000080"),
        INNHENTE_OPPLYSNINGER_OLD("000049"),
        ETTERLYS_INNTEKTSMELDING_OLD("000096"),

        // Gamle/utdaterte brevkoder med MF_ prefiks funnet i Joark
        VEDTAK_POSITIVT_OLD_MF("MF_000048"),
        VEDTAK_AVSLAG_OLD_MF("MF_000051"),
        VEDTAK_FORELDREPENGER_OLD_MF("MF_000061"),
        VEDTAK_AVSLAG_FORELDREPENGER_OLD_MF("MF_000080"),
        INNHENTE_OPPLYSNINGER_OLD_MF("MF_000049"),
        ETTERLYS_INNTEKTSMELDING_OLD_MF("MF_000096"),

        // Annet
        FORELDREPENGER_INFO_TIL_ANNEN_FORELDER("INFOAF"),
        VARSEL_OM_REVURDERING("VARREV"),
        INFO_OM_HENLEGGELSE("IOHENL"),
        INNSYN_SVAR("INNSYN"),
        IKKE_SØKT("IKKESO"),
        INGEN_ENDRING("INGEND"),
        FORLENGET_SAKSBEHANDLINGSTID("FORSAK"),
        FORLENGET_SAKSBEHANDLINGSTID_MEDL("FORMED"),
        FORLENGET_SAKSBEHANDLINGSTID_TIDLIG("FORTID"),
        KLAGE_AVVIST("KGEAVV"),
        KLAGE_OMGJORT("KGEOMG"),
        KLAGE_OVERSENDT("KGEOVE"),

        UKJENT("UKJENT"),
        URELEVANT("URELEVANT");

        private String kode;

        Brevkode(String kode) {
            this.kode = kode;
        }

        public String kode() {
            return kode;
        }

        public static Brevkode fraKode(String kode) {
            return Arrays.stream(values())
                .filter(brevkode -> brevkode.kode().equals(kode))
                .findFirst()
                .orElseThrow();
        }
    }
}
