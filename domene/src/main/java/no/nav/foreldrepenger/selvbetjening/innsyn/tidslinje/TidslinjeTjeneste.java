package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.EnkelJournalpost.Brevkode.FORELDREPENGER_FEIL_PRAKSIS_UTSETTELSE_INFOBREV;
import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.EnkelJournalpost.Brevkode.UKJENT;
import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.EnkelJournalpost.Type.INNGÅENDE_DOKUMENT;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import no.nav.foreldrepenger.selvbetjening.innsyn.InntektsmeldingDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.EnkelJournalpost;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.SafSelvbetjeningTjeneste;

@Service
public class TidslinjeTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeTjeneste.class);
    private static final String VARSEL_TILBAKEBETALING_TITTEL = "Varsel tilbakebetaling";

    private final SafSelvbetjeningTjeneste safselvbetjening;
    private final Innsyn innsyn;

    public TidslinjeTjeneste(SafSelvbetjeningTjeneste safselvbetjening, Innsyn innsyn) {
        this.safselvbetjening = safselvbetjening;
        this.innsyn = innsyn;
    }


    public List<TidslinjeHendelseDto> tidslinje(Fødselsnummer fødselsnummer, Saksnummer saksnummer) {
        var alleDokumenterFraSaf = safselvbetjening.alle(fødselsnummer, saksnummer);
        var mappedeDokumenter = alleDokumenterFraSaf.stream()
            .map(journalpost -> tilTidslinjeHendelse(journalpost, alleDokumenterFraSaf))
            .flatMap(Optional::stream);
        var mappedeInntektsmeldinger = innsyn.inntektsmeldinger(saksnummer).stream()
            .map(TidslinjeTjeneste::tilTidslinjeHendelse);
        return Stream.concat(mappedeDokumenter, mappedeInntektsmeldinger)
            .sorted(Comparator.comparing(TidslinjeHendelseDto::opprettet))
            .toList();
    }

    private static Optional<TidslinjeHendelseDto> tilTidslinjeHendelse(EnkelJournalpost enkelJournalpost, List<EnkelJournalpost> alleDokumentene) {
        if (enkelJournalpost.type().equals(EnkelJournalpost.Type.UTGÅENDE_DOKUMENT)) {
            return tidslinjeHendelseTypeUtgåendeDokument(enkelJournalpost)
                .map(hendelseType -> new TidslinjeHendelseDto(
                    enkelJournalpost.mottatt(),
                    TidslinjeHendelseDto.AktørType.NAV,
                    hendelseType,
                    tilDokumenter(enkelJournalpost.dokumenter(), enkelJournalpost.journalpostId())
                ));
        } else if (enkelJournalpost.type().equals(INNGÅENDE_DOKUMENT)) {
            return tidslinjehendelsetypeInngåendeDokument(enkelJournalpost, alleDokumentene)
                .map(hendelseType -> new TidslinjeHendelseDto(
                    enkelJournalpost.mottatt(),
                    TidslinjeHendelseDto.AktørType.BRUKER,
                    hendelseType,
                    tilDokumenter(enkelJournalpost.dokumenter(), enkelJournalpost.journalpostId())
                ));
        }
        throw new IllegalStateException("Utviklerfeil: Noe annet enn utgående eller inngående dokumenter skal ikke mappes og vises til bruker!");
    }

    private static Optional<TidslinjeHendelseDto.TidslinjeHendelseType> tidslinjeHendelseTypeUtgåendeDokument(EnkelJournalpost enkelJournalpost) {
        var brevkode = enkelJournalpost.dokumenter().stream().findFirst().orElseThrow().brevkode();
        if (brevkode.equals(UKJENT)) {
            LOG.info("Ignorerer utgåpende journalpost med ukjent brevkode: {}", enkelJournalpost);
        }

        if (brevkode.erVedtak()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK);
        } else if (brevkode.erFritekstbrev()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK); // TODO: Er riktig nå, men må mappe til riktig brevkode i fpformidling/fpdokgen
        } else if (brevkode.erKlageVedtak()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK_KLAGE);
        } else if (brevkode.erKlageSendtTilKlageinstansen()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_KLAGE_SENDT_TIL_KLAGEINSTANSEN);
        } else if (brevkode.erInnhentOpplysninger()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_INNHENT_OPPLYSNINGER);
        } else if (brevkode.erEtterlysIM()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_ETTERLYS_INNTEKTSMELDING);
        } else if (brevkode.erVarselOmTilbakebetaling() && enkelJournalpost.tittel().contains(VARSEL_TILBAKEBETALING_TITTEL)) {
            LOG.info("Varsel om tilbakebetaling returnes {}", enkelJournalpost);
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_VARSEL_TILBAKEBETALING);
        } else if (brevkode.equals(FORELDREPENGER_FEIL_PRAKSIS_UTSETTELSE_INFOBREV)) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.FORELDREPENGER_FEIL_PRAKSIS_UTSETTELSE_INFOBREV);
        } else {
            LOG.info("Ignorerer utgåpende journalpost pga filtering: {}", enkelJournalpost);
            return Optional.empty();
        }
    }

    private static Optional<TidslinjeHendelseDto.TidslinjeHendelseType> tidslinjehendelsetypeInngåendeDokument(EnkelJournalpost enkelJournalpost, List<EnkelJournalpost> alleDokumentene) {
        var dokumentType = enkelJournalpost.hovedtype();
        if (dokumentType == null) {
            LOG.info("Ignorer inngående journalpost i tidslinje for journalpost fordi dokumenttype er null: {}", enkelJournalpost);
            return Optional.empty();
        }
        if (dokumentType.erFørstegangssøknad()) {
            return Optional.of(erNyFørstegangssøknad(enkelJournalpost, alleDokumentene) ? TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD_NY :
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD);
        } else if (dokumentType.erEndringssøknad()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.ENDRINGSSØKNAD);
        } else if (dokumentType.erVedlegg() || dokumentType.erUttalelseOmTilbakekreving()) {
            return  Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.ETTERSENDING);
        } else if (dokumentType.equals(DokumentType.I000027)) {
            return  Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.KLAGE);
        } else {
            LOG.info("Ignorer inngående journalpost i tidslinje for journalpost pga filtrering: {}", enkelJournalpost);
            return Optional.empty();
        }
    }

    private static boolean erNyFørstegangssøknad(EnkelJournalpost enkelJournalpost, List<EnkelJournalpost> alleDokumentene) {
        return alleDokumentene.stream()
            .filter(j -> INNGÅENDE_DOKUMENT.equals(j.type()))
            .filter(journalpost -> journalpost.hovedtype() != null && journalpost.hovedtype().erFørstegangssøknad())
            .anyMatch(journalpost -> journalpost.mottatt().isBefore(enkelJournalpost.mottatt()));
    }

    private static List<TidslinjeHendelseDto.Dokument> tilDokumenter(List<EnkelJournalpost.Dokument> dokumenter, String journalpostId) {
        return dokumenter.stream()
            .map(dokument -> tilDokument(dokument, journalpostId))
            .toList();
    }

    private static TidslinjeHendelseDto.Dokument tilDokument(EnkelJournalpost.Dokument dokument, String journalpostId) {
        return new TidslinjeHendelseDto.Dokument(journalpostId, dokument.dokumentId(), dokument.tittel());
    }

    private static TidslinjeHendelseDto tilTidslinjeHendelse(InntektsmeldingDto inntektsmelding) {
        return new TidslinjeHendelseDto(
            inntektsmelding.opprettet(),
            TidslinjeHendelseDto.AktørType.ARBEIDSGIVER,
            TidslinjeHendelseDto.TidslinjeHendelseType.INNTEKTSMELDING,
            List.of()
        );
    }
}
