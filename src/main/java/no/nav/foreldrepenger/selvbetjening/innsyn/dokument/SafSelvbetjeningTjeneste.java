package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.safselvbetjening.Datotype;
import no.nav.safselvbetjening.DokumentInfo;
import no.nav.safselvbetjening.DokumentInfoResponseProjection;
import no.nav.safselvbetjening.DokumentoversiktResponseProjection;
import no.nav.safselvbetjening.DokumentoversiktSelvbetjeningQueryRequest;
import no.nav.safselvbetjening.DokumentvariantResponseProjection;
import no.nav.safselvbetjening.FagsakResponseProjection;
import no.nav.safselvbetjening.Journalpost;
import no.nav.safselvbetjening.JournalpostResponseProjection;
import no.nav.safselvbetjening.Journalposttype;
import no.nav.safselvbetjening.RelevantDato;
import no.nav.safselvbetjening.RelevantDatoResponseProjection;
import no.nav.safselvbetjening.SakResponseProjection;
import no.nav.safselvbetjening.Tema;

@Service
public class SafSelvbetjeningTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(SafSelvbetjeningTjeneste.class);
    private static final Set<Journalposttype> INKLUDER_JOURNALPOSTTYPER = Set.of(Journalposttype.I, Journalposttype.U);
    private static final List<String> GYLDIGE_FILFORMAT = List.of("PDF");
    //    private static final List<String> GYLDIGE_FILFORMAT = List.of("PDF", "JPG", "PNG"); // TODO

    private final SafSelvbetjeningConnection safKlient;

    public SafSelvbetjeningTjeneste(SafSelvbetjeningConnection safKlient) {
        this.safKlient = safKlient;
    }

    public ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return safKlient.hentDokument(journalpostId, dokumentId);
    }

    public List<EnkelJournalpost> alle(Fødselsnummer fnr) {
        var projection = new DokumentoversiktResponseProjection()
            .journalposter(journalpostProjeksjon());

        return safKlient.dokumentoversiktSelvbetjening(query(fnr), projection).getJournalposter().stream()
            .filter(j -> INKLUDER_JOURNALPOSTTYPER.contains(j.getJournalposttype()))
            .map(SafSelvbetjeningTjeneste::tilEnkelJournalpost)
            .flatMap(Optional::stream)
            .toList();
    }

    public List<EnkelJournalpost> alle(Fødselsnummer fnr, Saksnummer saksnummer) {
        var projection = new DokumentoversiktResponseProjection()
            .fagsak(fagsakProjektsjon());

        return safKlient.dokumentoversiktSelvbetjening(query(fnr), projection).getFagsak().stream()
            .filter(fagsak -> saksnummer.value().equals(fagsak.getFagsakId()))
            .flatMap(fagsak -> fagsak.getJournalposter().stream())
            .filter(j -> INKLUDER_JOURNALPOSTTYPER.contains(j.getJournalposttype()))
            .map(SafSelvbetjeningTjeneste::tilEnkelJournalpost)
            .flatMap(Optional::stream)
            .toList();
    }

    private static DokumentoversiktSelvbetjeningQueryRequest query(Fødselsnummer fnr) {
        var query = new DokumentoversiktSelvbetjeningQueryRequest();
        query.setIdent(fnr.value());
        query.setTema(List.of(Tema.FOR));
        return query;
    }

    private static Optional<EnkelJournalpost> tilEnkelJournalpost(Journalpost journalpost) {
        var pdfDokumenter = journalpost.getDokumenter().stream()
            .filter(dokumentInfo -> dokumentInfo.getDokumentvarianter().stream()
                .anyMatch(dokumentvariant -> dokumentvariant.getBrukerHarTilgang() && GYLDIGE_FILFORMAT.contains(dokumentvariant.getFiltype())))
            .toList();
        if (pdfDokumenter.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapTilJournalpost(journalpost, pdfDokumenter));
    }

    private static FagsakResponseProjection fagsakProjektsjon() {
        return new FagsakResponseProjection()
            .fagsakId()
            .fagsaksystem()
            .journalposter(journalpostProjeksjon());
    }

    private static JournalpostResponseProjection journalpostProjeksjon() {
        return new JournalpostResponseProjection()
            .tittel()
            .journalpostId()
            .journalposttype()
            .relevanteDatoer(new RelevantDatoResponseProjection()
                .dato()
                .datotype())
            .sak(new SakResponseProjection()
                .fagsakId()
                .fagsaksystem())
            .dokumenter(new DokumentInfoResponseProjection()
                .tittel()
                .dokumentInfoId()
                .brevkode()
                .dokumentvarianter(new DokumentvariantResponseProjection()
                    .variantformat()
                    .filtype()
                    .brukerHarTilgang()))
            .journalstatus();
    }

    private static EnkelJournalpost mapTilJournalpost(Journalpost journalpost, List<DokumentInfo> pdfDokument) {
        var innsendingstype = tilType(journalpost.getJournalposttype());
        return new EnkelJournalpost(
            journalpost.getTittel(),
            journalpost.getJournalpostId(),
            journalpost.getSak() != null ? journalpost.getSak().getFagsakId() : null,
            null,
            innsendingstype,
            tilOpprettetDato(journalpost.getRelevanteDatoer()),
            innsendingstype.equals(EnkelJournalpost.DokumentType.INNGÅENDE_DOKUMENT) ? dokumenttypeFraTittel(journalpost) : DokumentTypeId.URELEVANT,
            tilDokumenter(pdfDokument, journalpost.getJournalposttype())
        );
    }

    private static List<EnkelJournalpost.Dokument> tilDokumenter(List<DokumentInfo> pdfDokument, Journalposttype journalposttype) {
        return pdfDokument.stream()
            .map(d -> tilDokument(d, journalposttype))
            .toList();
    }

    private static EnkelJournalpost.Dokument tilDokument(DokumentInfo dokumentInfo, Journalposttype journalposttype) {
        if (journalposttype.equals(Journalposttype.U)) {
            return new EnkelJournalpost.Dokument(
                dokumentInfo.getDokumentInfoId(),
                dokumentInfo.getTittel(),
                tilBrevKode(dokumentInfo.getBrevkode()));
        } else {
            return new EnkelJournalpost.Dokument(
                dokumentInfo.getDokumentInfoId(),
                dokumentInfo.getTittel(),
                EnkelJournalpost.Brevkode.URELEVANT);
        }
    }

    private static EnkelJournalpost.Brevkode tilBrevKode(String brevkode) {
        try {
            return EnkelJournalpost.Brevkode.fraKode(brevkode);
        } catch (Exception e) {
            LOG.info("Ukjent brevkode {}", brevkode);
            return EnkelJournalpost.Brevkode.UKJENT;
        }
    }

    private static LocalDateTime tilOpprettetDato(List<RelevantDato> datoer) {
        if (datoer == null) {
            return null;
        }
        return datoer.stream()
            .filter(d -> Datotype.DATO_OPPRETTET.equals(d.getDatotype()))
            .map(SafSelvbetjeningTjeneste::tilLocalDateTime)
            .findFirst()
            .orElseThrow();
    }

    private static LocalDateTime tilLocalDateTime(RelevantDato relevantDato) {
        return relevantDato.getDato().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }

    private static EnkelJournalpost.DokumentType tilType(Journalposttype journalposttype) {
        return switch (journalposttype) {
            case I -> EnkelJournalpost.DokumentType.INNGÅENDE_DOKUMENT;
            case U -> EnkelJournalpost.DokumentType.UTGÅENDE_DOKUMENT;
            case N -> throw new IllegalStateException("Utviklerfeil: Skal filterer bort notater før mapping!");
        };
    }

    private static DokumentTypeId dokumenttypeFraTittel(Journalpost journalpost) {
        return utledFraTittel(journalpost.getTittel())
            .or(() -> utledFraTittel(journalpost.getDokumenter().stream().findFirst().orElseThrow().getTittel()))
            .orElse(DokumentTypeId.UKJENT);
    }

    private static Optional<DokumentTypeId> utledFraTittel(String tittel) {
        try {
            return Optional.of(DokumentTypeId.fraTittel(tittel));
        } catch (Exception e) {
            LOG.info("Klarte ikke utlede dokumentTypeId fra SAF tittel: {}", tittel);
            return Optional.empty();
        }
    }
}
