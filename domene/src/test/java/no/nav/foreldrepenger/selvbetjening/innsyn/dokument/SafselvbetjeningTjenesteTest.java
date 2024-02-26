package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.safselvbetjening.Datotype;
import no.nav.safselvbetjening.DokumentInfo;
import no.nav.safselvbetjening.Dokumentoversikt;
import no.nav.safselvbetjening.Dokumentvariant;
import no.nav.safselvbetjening.Fagsak;
import no.nav.safselvbetjening.Journalpost;
import no.nav.safselvbetjening.Journalposttype;
import no.nav.safselvbetjening.Journalstatus;
import no.nav.safselvbetjening.RelevantDato;
import no.nav.safselvbetjening.Sak;

class SafselvbetjeningTjenesteTest {

    private static final Fødselsnummer DUMMY_FNR = new Fødselsnummer("000000");
    private static final Saksnummer DUMMY_SAKSNUMMER = new Saksnummer("000000");

    private SafSelvbetjeningConnection saf;
    private SafSelvbetjeningTjeneste safselvbetjeningTjeneste;

    @BeforeEach
    void setUp() {
        saf = mock(SafSelvbetjeningConnection.class);
        safselvbetjeningTjeneste = new SafSelvbetjeningTjeneste(saf);
    }

    @Test
    void skalIkkeReturnereJournalposterAvTypenNotat() {
        var journalførtSøknad = journalførtSøknad(DokumentType.I000001);
        var journalførtNotat = notat();
        var journalposterFraSaf = List.of(journalførtSøknad, journalførtNotat);
        var fagsak = new Fagsak(journalposterFraSaf, DUMMY_SAKSNUMMER.value(), null, null);
        var dokumentoversikt = new Dokumentoversikt(null, List.of(fagsak), null);
        when(saf.dokumentoversiktSelvbetjening(any(), any())).thenReturn(dokumentoversikt);

        var journalposter = safselvbetjeningTjeneste.alle(DUMMY_FNR, DUMMY_SAKSNUMMER);

        assertThat(journalposter).hasSize(1);
    }


    @Test
    void skalBareReturnerJournalposterMedDokumenterAvTypenPDF() {
        var journalførtSøknad = journalførtSøknad(DokumentType.I000001);
        var journalførtEttersending = journalførtEttersending();
        var journalførtVedtak = journalførtVedtak();
        var journalførtDokumentBareXML = journalførtDokumentBareXML();
        var journalposterFraSaf = List.of(journalførtSøknad, journalførtEttersending, journalførtVedtak, journalførtDokumentBareXML);
        var fagsak = new Fagsak(journalposterFraSaf, DUMMY_SAKSNUMMER.value(), null, null);
        var dokumentoversikt = new Dokumentoversikt(null, List.of(fagsak), null);
        when(saf.dokumentoversiktSelvbetjening(any(), any())).thenReturn(dokumentoversikt);

        var journalposter = safselvbetjeningTjeneste.alle(DUMMY_FNR, DUMMY_SAKSNUMMER);

        assertThat(journalposter)
            .hasSize(journalposterFraSaf.size() - 1)
            .extracting(EnkelJournalpost::dokumenter)
            .noneMatch(List::isEmpty);
        var journalførtSøkad = journalposter.stream()
            .filter(j -> j.hovedtype().erFørstegangssøknad())
            .findFirst()
            .orElseThrow();
        assertThat(journalførtSøkad.dokumenter()).hasSize(1); // XML dokument skal ikke returneres
    }


    @Test
    void skalUtledeDokumentTypeIdFraTittelPåDokumentetHvisIkkeJournalpostTittelenTreffer() {
        var dokumentTypeId = DokumentType.I000001;
        var journalposterFraSaf = List.of(journalførtSøknadRarTittelPåJournalpostRiktigTittelPåDokument(dokumentTypeId));
        var fagsak = new Fagsak(journalposterFraSaf, DUMMY_SAKSNUMMER.value(), null, null);
        var dokumentoversikt = new Dokumentoversikt(null, List.of(fagsak), null);
        when(saf.dokumentoversiktSelvbetjening(any(), any())).thenReturn(dokumentoversikt);

        var journalposter = safselvbetjeningTjeneste.alle(DUMMY_FNR, DUMMY_SAKSNUMMER);

        assertThat(journalposter)
            .hasSize(journalposterFraSaf.size())
            .extracting(EnkelJournalpost::dokumenter)
            .noneMatch(List::isEmpty);


        var journalførtSøkad = journalposter.stream()
            .filter(j -> j.hovedtype().erFørstegangssøknad())
            .findFirst()
            .orElseThrow();
        assertThat(journalførtSøkad.hovedtype()).isEqualTo(dokumentTypeId);
    }

    @Test
    void skalUtledeFraDokumentTittelHvisAltEllersFeiler() {
        var dokumentTypeId = DokumentType.I000001;
        var journalposterFraSaf = List.of(journalpostUgyldigTittelUtenTilleggsinfoMenRiktigDokumentTittel(dokumentTypeId));
        var fagsak = new Fagsak(journalposterFraSaf, DUMMY_SAKSNUMMER.value(), null, null);
        var dokumentoversikt = new Dokumentoversikt(null, List.of(fagsak), null);
        when(saf.dokumentoversiktSelvbetjening(any(), any())).thenReturn(dokumentoversikt);

        var journalposter = safselvbetjeningTjeneste.alle(DUMMY_FNR, DUMMY_SAKSNUMMER);

        assertThat(journalposter)
            .hasSize(journalposterFraSaf.size())
            .extracting(EnkelJournalpost::dokumenter)
            .noneMatch(List::isEmpty);

        var journalførtSøkad = journalposter.stream()
            .filter(j -> j.hovedtype().erFørstegangssøknad())
            .findFirst()
            .orElseThrow();
        assertThat(journalførtSøkad.hovedtype()).isEqualTo(dokumentTypeId);
    }

    @Test
    void skalIkkeReturnereDokumenterHvorBrukerIkkeHarTilgang() {
        var dokumentTypeId = DokumentType.I000001;
        var journalposterFraSaf = List.of(journalførtDokumentBrukerIkkeTilgang(dokumentTypeId));
        var fagsak = new Fagsak(journalposterFraSaf, DUMMY_SAKSNUMMER.value(), null, null);
        var dokumentoversikt = new Dokumentoversikt(null, List.of(fagsak), null);
        when(saf.dokumentoversiktSelvbetjening(any(), any())).thenReturn(dokumentoversikt);

        var journalposter = safselvbetjeningTjeneste.alle(DUMMY_FNR, DUMMY_SAKSNUMMER);

        assertThat(journalposter).isEmpty();
    }

    @Test
    void skalReturnerAlleJournalposterUavhengigAvSaksnummer() {
        var journalpostMedSaksnummer = journalførtSøknad(DokumentType.I000001);
        var journalpostMedAnnnetSaksnummer = journalførtSøknad(DokumentType.I000001);
        journalpostMedAnnnetSaksnummer.setSak(new Sak(DUMMY_SAKSNUMMER.value(), null, null));
        var journalposterFraSaf = List.of(journalpostMedSaksnummer, journalpostMedAnnnetSaksnummer);
        var dokumentoversikt = new Dokumentoversikt(null, null, journalposterFraSaf);
        when(saf.dokumentoversiktSelvbetjening(any(), any())).thenReturn(dokumentoversikt);

        var journalposter = safselvbetjeningTjeneste.alle(DUMMY_FNR);

        assertThat(journalposter).hasSameSizeAs(journalposterFraSaf);
    }

    @Test
    void skalReturnerJournalposterSomIkkeHarEnSakstilknyning() {
        var journalposterFraSaf = List.of(journalpostUtenSakstilknytning(DokumentType.I000001));
        var dokumentoversikt = new Dokumentoversikt(null, null, journalposterFraSaf);
        when(saf.dokumentoversiktSelvbetjening(any(), any())).thenReturn(dokumentoversikt);

        var journalposter = safselvbetjeningTjeneste.alle(DUMMY_FNR);

        assertThat(journalposter).hasSameSizeAs(journalposterFraSaf);
    }


    private static Journalpost notat() {
        var journalførtNotat = new Journalpost();
        journalførtNotat.setJournalposttype(Journalposttype.N);
        return journalførtNotat;
    }

    private static Journalpost journalførtSøknad(DokumentType dokumentTypeId) {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.I);
        journalpost.setJournalstatus(Journalstatus.MOTTATT);
        journalpost.setTittel(dokumentTypeId.getTittel());
        journalpost.setJournalpostId("123");
        var sak = new Sak();
        sak.setFagsakId(DUMMY_SAKSNUMMER.value());
        journalpost.setSak(sak);
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(pdfDokument(dokumentTypeId), xmlDokument(dokumentTypeId)));
        return journalpost;
    }

    private static Journalpost journalførtDokumentBrukerIkkeTilgang(DokumentType dokumentTypeId) {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.I);
        journalpost.setJournalstatus(Journalstatus.MOTTATT);
        journalpost.setTittel(dokumentTypeId.getTittel());
        journalpost.setJournalpostId("123");
        var sak = new Sak();
        sak.setFagsakId(DUMMY_SAKSNUMMER.value());
        journalpost.setSak(sak);
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(pdfDokumentBrukerIkkeTilgang(dokumentTypeId)));
        return journalpost;
    }

    private static Journalpost journalførtSøknadRarTittelPåJournalpostRiktigTittelPåDokument(DokumentType dokumentTypeId) {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.I);
        journalpost.setJournalstatus(Journalstatus.MOTTATT);
        journalpost.setTittel("EN UGYLDIG TITTEL");
        journalpost.setJournalpostId("123");
        var sak = new Sak();
        sak.setFagsakId(DUMMY_SAKSNUMMER.value());
        journalpost.setSak(sak);
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(pdfDokument(dokumentTypeId), xmlDokument(dokumentTypeId)));
        return journalpost;
    }

    private static Journalpost journalpostUgyldigTittelUtenTilleggsinfoMenRiktigDokumentTittel(DokumentType dokumentTypeId) {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.I);
        journalpost.setJournalstatus(Journalstatus.MOTTATT);
        journalpost.setTittel("FEIL_TITTEL");
        journalpost.setJournalpostId("123");
        var sak = new Sak();
        sak.setFagsakId(DUMMY_SAKSNUMMER.value());
        journalpost.setSak(sak);
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(pdfDokument(dokumentTypeId), xmlDokument(dokumentTypeId)));
        return journalpost;
    }

    private static Journalpost journalpostUtenSakstilknytning(DokumentType dokumentTypeId) {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.I);
        journalpost.setJournalstatus(Journalstatus.MOTTATT);
        journalpost.setTittel("FEIL_TITTEL");
        journalpost.setJournalpostId("123");
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(pdfDokument(dokumentTypeId), xmlDokument(dokumentTypeId)));
        return journalpost;
    }

    private static Journalpost journalførtEttersending() {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.I);
        journalpost.setJournalstatus(Journalstatus.MOTTATT);
        var dokumentType = DokumentType.I000042;
        journalpost.setTittel(dokumentType.getTittel());
        journalpost.setJournalpostId("123");
        var sak = new Sak();
        sak.setFagsakId(DUMMY_SAKSNUMMER.value());
        journalpost.setSak(sak);
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(pdfDokument(dokumentType), pdfDokument(DokumentType.I000045)));
        return journalpost;
    }

    private static Journalpost journalførtVedtak() {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.U);
        journalpost.setJournalstatus(Journalstatus.EKSPEDERT);
        journalpost.setTittel("Vedtak");
        journalpost.setJournalpostId("123");
        var sak = new Sak();
        sak.setFagsakId(DUMMY_SAKSNUMMER.value());
        journalpost.setSak(sak);
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(pdfDokument("INVFOR")));
        return journalpost;
    }

    private static Journalpost journalførtDokumentBareXML() {
        var journalpost = new Journalpost();
        journalpost.setJournalposttype(Journalposttype.I);
        journalpost.setJournalstatus(Journalstatus.MOTTATT);
        var dokumentType = DokumentType.I000060;
        journalpost.setTittel(dokumentType.getTittel());
        journalpost.setJournalpostId("123");
        var sak = new Sak();
        sak.setFagsakId(DUMMY_SAKSNUMMER.value());
        journalpost.setSak(sak);
        journalpost.setRelevanteDatoer(List.of(new RelevantDato(LocalDateTime.now(), Datotype.DATO_OPPRETTET)));
        journalpost.setDokumenter(List.of(xmlDokument(dokumentType)));
        return journalpost;
    }

    private static DokumentInfo pdfDokument(String brevKode) {
        var dokument = new DokumentInfo();
        dokument.setDokumentInfoId("123");
        dokument.setTittel("Innvilgelsesbrev Foreldrepenger");
        dokument.setBrevkode(brevKode);
        var dokumentvariant = new Dokumentvariant();
        dokumentvariant.setFiltype("PDF");
        dokumentvariant.setBrukerHarTilgang(true);
        dokument.setDokumentvarianter(List.of(dokumentvariant));
        return dokument;
    }

    private static DokumentInfo pdfDokument(DokumentType dokumentTypeId) {
        var dokument = new DokumentInfo();
        dokument.setDokumentInfoId("123");
        dokument.setTittel(dokumentTypeId.getTittel());
        dokument.setBrevkode(null);
        var dokumentvariant = new Dokumentvariant();
        dokumentvariant.setFiltype("PDF");
        dokumentvariant.setBrukerHarTilgang(true);
        dokument.setDokumentvarianter(List.of(dokumentvariant));
        return dokument;
    }

    private static DokumentInfo pdfDokumentBrukerIkkeTilgang(DokumentType dokumentTypeId) {
        var dokument = new DokumentInfo();
        dokument.setDokumentInfoId("123");
        dokument.setTittel(dokumentTypeId.getTittel());
        dokument.setBrevkode(null);
        var dokumentvariant = new Dokumentvariant();
        dokumentvariant.setFiltype("PDF");
        dokumentvariant.setBrukerHarTilgang(false);
        dokument.setDokumentvarianter(List.of(dokumentvariant));
        return dokument;
    }

    private static DokumentInfo xmlDokument(DokumentType dokumentTypeId) {
        var dokument = new DokumentInfo();
        dokument.setDokumentInfoId("456");
        dokument.setTittel(dokumentTypeId.getTittel());
        dokument.setBrevkode(null);
        var dokumentvariant = new Dokumentvariant();
        dokumentvariant.setFiltype("XML");
        dokumentvariant.setBrukerHarTilgang(true);
        dokument.setDokumentvarianter(List.of(dokumentvariant));
        return dokument;
    }

}
