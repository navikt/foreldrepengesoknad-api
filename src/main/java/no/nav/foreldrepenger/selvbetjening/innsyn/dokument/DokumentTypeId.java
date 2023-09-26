package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.util.Arrays;
import java.util.Set;

public enum DokumentTypeId {
    // Søknader
    I000001("Søknad om svangerskapspenger"),
    I000002("Søknad om foreldrepenger ved adopsjon"),
    I000003("Søknad om engangsstønad ved fødsel"),
    I000004("Søknad om engangsstønad ved adopsjon"),
    I000005("Søknad om foreldrepenger ved fødsel"),
    I000006("Utsettelse eller gradert uttak av foreldrepenger (fleksibelt uttak)"),
    I000050("Søknad om endring av uttak av foreldrepenger eller overføring av kvote"),

    // Klage
    I000027("Klage/anke"),

    // Tilbakekreving
    I000114("Uttalelse tilbakekreving"),

    // Inntekt
    I000067("Inntektsmelding"),

    // Vedlegg fra brukerdialog - brukes i opplysningsplikt (ManglendeVedlegg)
    I000007("Inntektsopplysninger om selvstendig næringsdrivende og/eller frilansere som skal ha foreldrepenger eller svangerskapspenger"),
    I000023("Legeerklæring"),
    I000032("Resultatregnskap"),
    I000036("Dokumentasjon av ferie"),
    I000037("Dokumentasjon av innleggelse i helseinstitusjon"),
    I000038("Dokumentasjon av mors utdanning, arbeid eller sykdom"),
    I000039("Dokumentasjon av militær- eller siviltjeneste"),
    I000041("Dokumentasjon av termindato (lev. kun av mor), fødsel eller dato for omsorgsovertakelse"),
    I000042("Dokumentasjon av dato for overtakelse av omsorg"),
    I000044("Dokumentasjon av etterlønn/sluttvederlag"),
    I000045("Beskrivelse av funksjonsnedsettelse"),
    I000049("Annet skjema (ikke NAV-skjema)"),
    I000051("Bekreftelse på deltakelse i kvalifiseringsprogrammet"),
    I000060("Annet"),
    I000061("Bekreftelse fra studiested/skole"),
    I000062("Bekreftelse på ventet fødselsdato"),
    I000063("Fødselsattest"),
    I000064("Elevdokumentasjon fra lærested"),
    I000065("Bekreftelse fra arbeidsgiver"),
    I000066("Kopi av likningsattest eller selvangivelse"),
    I000109("Skjema for tilrettelegging og omplassering ved graviditet"),
    I000110("Dokumentasjon av aleneomsorg"),
    I000111("Dokumentasjon av begrunnelse for hvorfor man søker tilbake i tid"),
    I000112("Dokumentasjon av deltakelse i introduksjonsprogrammet"),
    I000116("Bekreftelse på øvelse eller tjeneste i Forsvaret eller Sivilforsvaret"),
    I000117("Bekreftelse på tiltak i regi av Arbeids- og velferdsetaten"),

    UKJENT("Ukjent"),
    URELEVANT("Urelevant");

    private static final Set<DokumentTypeId> VEDLEGG_TYPER = Set.of(
            I000007,
            I000023,
            I000032,
            I000036,
            I000037,
            I000038,
            I000039,
            I000041,
            I000042,
            I000044,
            I000045,
            I000049,
            I000051,
            I000060,
            I000061,
            I000062,
            I000063,
            I000064,
            I000065,
            I000066,
            I000109,
            I000110,
            I000111,
            I000112,
            I000116,
            I000117
    );

    public static final Set<DokumentTypeId> FØRSTEGANGSSØKNAD_TYPER = Set.of(
            I000001,
            I000002,
            I000003,
            I000004,
            I000005,
            I000006
    );


    public static final Set<DokumentTypeId> ENDRINGSSØKNAD_TYPER = Set.of(I000050);


    private final String tittel;

    DokumentTypeId(String tittel) {
        this.tittel = tittel;
    }

    public static DokumentTypeId fraTittel(String tittel) {
        return Arrays.stream(values())
            .filter(dokumentTypeId -> dokumentTypeId.getTittel().equals(tittel))
            .findFirst()
            .orElseThrow();
    }

    public String getTittel() {
        return tittel;
    }

    public boolean erFørstegangssøknad() {
        return FØRSTEGANGSSØKNAD_TYPER.contains(this);
    }

    public boolean erEndringssøknad() {
        return ENDRINGSSØKNAD_TYPER.contains(this);
    }

    public boolean erVedlegg() {
        return VEDLEGG_TYPER.contains(this);
    }

    public boolean erInntektsmelding() {
        return I000067.equals(this);
    }

    public boolean erUttalelseOmTilbakekreving() {
        return I000114.equals(this);
    }
}
