package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public record EnkelJournalpost(String tittel,
                               String journalpostId,
                               String saksnummer,
                               DokumentType type,
                               LocalDateTime mottatt,
                               DokumentTypeId hovedtype,
                               List<Dokument> dokumenter) {
    public enum DokumentType {
        INNGÅENDE_DOKUMENT,
        UTGÅENDE_DOKUMENT
    }

    public record Dokument(String dokumentId, String tittel, Brevkode brevkode) {
    }

    public enum Brevkode {
        // VEDTAK
        FORELDREPENGER_ANNULLERT("ANUFOR"),
        FORELDREPENGER_AVSLAG("AVSFOR"),
        SVANGERSKAPSPENGER_OPPHØR("OPPSVP"),
        ENGANGSSTØNAD_INNVILGELSE("INNVES"),
        SVANGERSKAPSPENGER_AVSLAG("AVSSVP"),
        FORELDREPENGER_INNVILGELSE("INVFOR"),
        ENGANGSSTØNAD_AVSLAG("AVSLES"),
        FORELDREPENGER_OPPHØR("OPPFOR"),
        SVANGERSKAPSPENGER_INNVILGELSE("INVSVP"),

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

        FRITEKSTBREV("FRITEK"), // Bare vedtak p.d.
        INNHENTE_OPPLYSNINGER("INNOPP"),
        ETTERLYS_INNTEKTSMELDING("ELYSIM"),
        FORELDREPENGER_FEIL_PRAKSIS_UTSETTELSE_INFOBREV("INFOPU"),
        VARSEL_TILBAKEBETALING_FP("FP-TILB"),
        VARSEL_TILBAKEBETALING_SVP("SVP-TILB"),
        VARSEL_TILBAKEBETALING_ES("ES-TILB"),

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

        private static final Set<Brevkode> VEDTAK_TYPER = Set.of(
            FORELDREPENGER_ANNULLERT,
            FORELDREPENGER_AVSLAG,
            SVANGERSKAPSPENGER_OPPHØR,
            ENGANGSSTØNAD_INNVILGELSE,
            SVANGERSKAPSPENGER_AVSLAG,
            FORELDREPENGER_INNVILGELSE,
            ENGANGSSTØNAD_AVSLAG,
            FORELDREPENGER_OPPHØR,
            SVANGERSKAPSPENGER_INNVILGELSE,
            VEDTAK_POSITIVT_OLD,
            VEDTAK_AVSLAG_OLD,
            VEDTAK_FORELDREPENGER_OLD,
            VEDTAK_AVSLAG_FORELDREPENGER_OLD,
            VEDTAK_POSITIVT_OLD_MF,
            VEDTAK_AVSLAG_OLD_MF,
            VEDTAK_FORELDREPENGER_OLD_MF,
            VEDTAK_AVSLAG_FORELDREPENGER_OLD_MF
        );


        private static final Set<Brevkode> INNHENT_OPPLYSNING_TYPER = Set.of(
            INNHENTE_OPPLYSNINGER,
            INNHENTE_OPPLYSNINGER_OLD,
            INNHENTE_OPPLYSNINGER_OLD_MF
        );


        private static final Set<Brevkode> ETTERLYS_INNTEKTSMELDING_TYPER = Set.of(
            ETTERLYS_INNTEKTSMELDING,
            ETTERLYS_INNTEKTSMELDING_OLD,
            ETTERLYS_INNTEKTSMELDING_OLD_MF
        );


        private static final Set<Brevkode> VARSEL_OM_TILBAKEBETALING = Set.of(
            VARSEL_TILBAKEBETALING_FP,
            VARSEL_TILBAKEBETALING_SVP,
            VARSEL_TILBAKEBETALING_ES
        );

        public boolean erVedtak() {
            return VEDTAK_TYPER.contains(this);
        }

        public boolean erInnhentOpplysninger() {
            return INNHENT_OPPLYSNING_TYPER.contains(this);
        }

        public boolean erEtterlysIM() {
            return ETTERLYS_INNTEKTSMELDING_TYPER.contains(this);
        }

        public boolean erVarselOmTilbakebetaling() {
            return VARSEL_OM_TILBAKEBETALING.contains(this);
        }

        public boolean erFritekstbrev() {
            return FRITEKSTBREV.equals(this);
        }

    }
}
