package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import no.nav.foreldrepenger.common.domain.felles.DokumentType;

public record EnkelJournalpost(String tittel,
                               String journalpostId,
                               String saksnummer,
                               Type type,
                               LocalDateTime mottatt,
                               DokumentType hovedtype,
                               List<Dokument> dokumenter) {
    public enum Type {
        INNGÅENDE_DOKUMENT,
        UTGÅENDE_DOKUMENT
    }

    public record Dokument(String dokumentId, String tittel, Brevkode brevkode) {
    }

    public enum Brevkode {
        FRITEKSTBREV("FRITEK"),
        ENGANGSSTØNAD_INNVILGELSE("INNVES"),
        ENGANGSSTØNAD_AVSLAG("AVSLES"),
        FORELDREPENGER_INNVILGELSE("INVFOR"),
        FORELDREPENGER_AVSLAG("AVSFOR"),
        FORELDREPENGER_OPPHØR("OPPFOR"),
        FORELDREPENGER_ANNULLERT("ANUFOR"),
        FORELDREPENGER_INFOBREV_TIL_ANNEN_FORELDER("INFOAF"),
        SVANGERSKAPSPENGER_INNVILGELSE("INVSVP"),
        SVANGERSKAPSPENGER_OPPHØR("OPPSVP"),
        SVANGERSKAPSPENGER_AVSLAG("AVSSVP"),
        INNHENTE_OPPLYSNINGER("INNOPP"),
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
        ETTERLYS_INNTEKTSMELDING("ELYSIM"),
        ENDRING_UTBETALING("ENDUTB"),
        FORELDREPENGER_FEIL_PRAKSIS_UTSETTELSE_INFOBREV("INFOPU"),

        // GAMLE
        FRITEKSTBREV_DOK("FRITKS"),
        ENGANGSSTØNAD_INNVILGELSE_DOK("POSVED"),
        ENGANGSSTØNAD_AVSLAG_DOK("AVSLAG"),
        FORELDREPENGER_INNVILGELSE_DOK("INNVFP"),
        FORELDREPENGER_AVSLAG_DOK("AVSLFP"),
        FORELDREPENGER_OPPHØR_DOK("OPPHOR"),
        FORELDREPENGER_INFOBREV_TIL_ANNEN_FORELDER_DOK("INAFOR"),
        SVANGERSKAPSPENGER_INNVILGELSE_FRITEKST("INNSVP"),
        INNHENTE_OPPLYSNINGER_DOK("INNHEN"),
        VARSEL_OM_REVURDERING_DOK("REVURD"),
        INFO_OM_HENLEGGELSE_DOK("HENLEG"),
        INNSYN_SVAR_DOK("INSSKR"),
        IKKE_SØKT_DOK("INNTID"),
        INGEN_ENDRING_DOK("UENDRE"),
        FORLENGET_SAKSBEHANDLINGSTID_DOK("FORLEN"),
        FORLENGET_SAKSBEHANDLINGSTID_MEDL_DOK("FORLME"),
        FORLENGET_SAKSBEHANDLINGSTID_TIDLIG_DOK("FORLTS"),
        KLAGE_AVVIST_DOK("KLAGAV"),
        KLAGE_AVVIST_FRITEKST("KAVVIS"),
        KLAGE_HJEMSENDT_DOK("KLAGNY"),
        KLAGE_HJEMSENDT_FRITEKST("KHJEMS"),
        KLAGE_OMGJORT_DOK("VEDMED"),
        KLAGE_OMGJORT_FRITEKST("KOMGJO"),
        KLAGE_OVERSENDT_DOK("KLAGOV"),
        KLAGE_OVERSENDT_FRITEKST("KOVKLA"),
        KLAGE_STADFESTET_DOK("KLAGVE"),
        KLAGE_STADFESTET_FRITEKST("KSTADF"),
        ANKE_OMGJORT_FRITEKST("VEDOGA"),
        ANKE_OPPHEVET_FRITEKST("ANKEBO"),
        ETTERLYS_INNTEKTSMELDING_FRITEKST("INNLYS"),
        ANKE_OMGJORT("ANKOMG"),
        ANKE_OPPHEVET("ANKOPP"),
        KLAGE_STADFESTET("KGESTA"),
        KLAGE_HJEMSENDT("KGEHJE"),

        // Gamle/utdaterte brevkoder funnet i Joark
        VEDTAK_POSITIVT_OLD("000048"),
        VEDTAK_POSITIVT_OLD_MF("MF_000048"),
        VEDTAK_AVSLAG_OLD("000051"),
        VEDTAK_AVSLAG_OLD_MF("MF_000051"),
        VEDTAK_FORELDREPENGER_OLD("000061"),
        VEDTAK_FORELDREPENGER_OLD_MF("MF_000061"),
        VEDTAK_AVSLAG_FORELDREPENGER_OLD("000080"),
        VEDTAK_AVSLAG_FORELDREPENGER_OLD_MF("MF_000080"),
        INNHENTE_OPPLYSNINGER_OLD("000049"),
        INNHENTE_OPPLYSNINGER_OLD_MF("MF_000049"),
        ETTERLYS_INNTEKTSMELDING_OLD("000096"),
        ETTERLYS_INNTEKTSMELDING_OLD_MF("MF_000096"),

        // Varsel om tilbakebetaling
        VARSEL_TILBAKEBETALING_FP("FP-TILB"),
        VARSEL_TILBAKEBETALING_SVP("SVP-TILB"),
        VARSEL_TILBAKEBETALING_ES("ES-TILB"),

        // Annet
        FORELDREPENGER_INFO_TIL_ANNEN_FORELDER("INFOAF"),
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
            ENGANGSSTØNAD_INNVILGELSE,
            ENGANGSSTØNAD_AVSLAG,
            FORELDREPENGER_INNVILGELSE,
            FORELDREPENGER_AVSLAG,
            FORELDREPENGER_OPPHØR,
            FORELDREPENGER_ANNULLERT,
            SVANGERSKAPSPENGER_INNVILGELSE,
            SVANGERSKAPSPENGER_OPPHØR,
            SVANGERSKAPSPENGER_AVSLAG,

            // Deprecated vedtakstyper i formidling
            ENGANGSSTØNAD_INNVILGELSE_DOK,
            ENGANGSSTØNAD_AVSLAG_DOK,
            FORELDREPENGER_INNVILGELSE_DOK,
            FORELDREPENGER_AVSLAG_DOK,
            FORELDREPENGER_OPPHØR_DOK,
            SVANGERSKAPSPENGER_INNVILGELSE_FRITEKST,

            // Eldre funnet i SAF
            VEDTAK_POSITIVT_OLD,
            VEDTAK_POSITIVT_OLD_MF,
            VEDTAK_AVSLAG_OLD,
            VEDTAK_AVSLAG_OLD_MF,
            VEDTAK_FORELDREPENGER_OLD,
            VEDTAK_FORELDREPENGER_OLD_MF,
            VEDTAK_AVSLAG_FORELDREPENGER_OLD,
            VEDTAK_AVSLAG_FORELDREPENGER_OLD_MF
        );


        private static final Set<Brevkode> INNHENT_OPPLYSNING_TYPER = Set.of(
            INNHENTE_OPPLYSNINGER,
            INNHENTE_OPPLYSNINGER_DOK,
            INNHENTE_OPPLYSNINGER_OLD,
            INNHENTE_OPPLYSNINGER_OLD_MF
        );


        private static final Set<Brevkode> ETTERLYS_INNTEKTSMELDING_TYPER = Set.of(
            ETTERLYS_INNTEKTSMELDING,
            ETTERLYS_INNTEKTSMELDING_FRITEKST,
            ETTERLYS_INNTEKTSMELDING_OLD,
            ETTERLYS_INNTEKTSMELDING_OLD_MF
        );


        private static final Set<Brevkode> VARSEL_OM_TILBAKEBETALING = Set.of(
            VARSEL_TILBAKEBETALING_FP,
            VARSEL_TILBAKEBETALING_SVP,
            VARSEL_TILBAKEBETALING_ES
        );

        private static final Set<Brevkode> KLAGE_SENDT_TIL_KLAGEINSTANSEN = Set.of(
            KLAGE_OVERSENDT_DOK,
            KLAGE_OVERSENDT_FRITEKST,
            KLAGE_OVERSENDT
        );
        private static final Set<Brevkode> VEDTAK_KLAGE = Set.of(
            KLAGE_AVVIST,
            KLAGE_OMGJORT,
            KLAGE_AVVIST_DOK,
            KLAGE_AVVIST_FRITEKST,
            KLAGE_HJEMSENDT_DOK,
            KLAGE_HJEMSENDT_FRITEKST,
            KLAGE_OMGJORT_DOK,
            KLAGE_OMGJORT_FRITEKST,
            KLAGE_STADFESTET_DOK,
            KLAGE_STADFESTET_FRITEKST,
            ANKE_OMGJORT_FRITEKST,
            ANKE_OPPHEVET_FRITEKST,
            ETTERLYS_INNTEKTSMELDING_FRITEKST,
            ANKE_OMGJORT,
            ANKE_OPPHEVET,
            KLAGE_STADFESTET,
            KLAGE_HJEMSENDT
        );

        public boolean erVedtak() {
            return VEDTAK_TYPER.contains(this);
        }

        public boolean erKlageVedtak() {
            return VEDTAK_KLAGE.contains(this);
        }

        public boolean erKlageSendtTilKlageinstansen() {
            return KLAGE_SENDT_TIL_KLAGEINSTANSEN.contains(this);
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
