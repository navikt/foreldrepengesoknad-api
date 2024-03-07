package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.util.Map;
import java.util.Optional;

import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.safselvbetjening.Journalpost;

public final class DokumentTypeUtleder {

    private DokumentTypeUtleder() {
        // Statisk implementasjon
    }

    public static DokumentType dokumenttypeFraTittel(Journalpost journalpost) {
        return utledFraTittel(journalpost.getTittel())
            .or(() -> utledFraTittel(journalpost.getDokumenter().stream().findFirst().orElseThrow().getTittel()))
            .orElse(null);
    }

    private static Optional<DokumentType> utledFraTittel(String tittel) {
        try {
            return Optional.of(DokumentType.fraTittel(tittel))
                .or(() -> Optional.ofNullable(ALT_TITLER.get(tittel)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static final Map<String, DokumentType> ALT_TITLER = Map.ofEntries(
        Map.entry("Søknad om svangerskapspenger til selvstendig næringsdrivende og frilanser", DokumentType.I000001),
        Map.entry("Søknad om svangerskapspenger for selvstendig", DokumentType.I000001),
        Map.entry("Bekreftelse på avtalt ferie", DokumentType.I000036),
        Map.entry("Mor er innlagt i helseinstitusjon", DokumentType.I000037),
        Map.entry("Mor er i arbeid, tar utdanning eller er for syk til å ta seg av barnet", DokumentType.I000038),
        Map.entry("Dokumentasjon av termindato, fødsel eller dato for omsorgsovertakelse", DokumentType.I000041),
        Map.entry("Tjenestebevis", DokumentType.I000039),
        Map.entry("Dokumentasjon av overtakelse av omsorg", DokumentType.I000042),
        Map.entry("Dokumentasjon av etterlønn eller sluttvederlag", DokumentType.I000044),
        Map.entry("Beskrivelse/Dokumentasjon funksjonsnedsettelse", DokumentType.I000045),
        Map.entry("Mor deltar i kvalifiseringsprogrammet", DokumentType.I000051),
        Map.entry("Mor tar utdanning på heltid", DokumentType.I000061),
        Map.entry("Kopi av skattemelding", DokumentType.I000066),
        Map.entry("Svar på varsel om tilbakebetaling", DokumentType.I000114),
        Map.entry("Klage", DokumentType.I000027),
        Map.entry("Anke", DokumentType.I000027),
        Map.entry("Rettskjennelse fra Trygderetten", DokumentType.I000027));
}
