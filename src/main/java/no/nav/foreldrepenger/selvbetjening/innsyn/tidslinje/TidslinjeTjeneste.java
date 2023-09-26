package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import static no.nav.foreldrepenger.selvbetjening.innsyn.dokument.EnkelJournalpost.DokumentType.INNGÅENDE_DOKUMENT;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsyn.Innsyn;
import no.nav.foreldrepenger.selvbetjening.innsyn.InntektsmeldingDto;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.EnkelJournalpost;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.SafSelvbetjeningTjeneste;

@Service
public class TidslinjeTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeTjeneste.class);

    private final SafSelvbetjeningTjeneste safselvbetjening;
    private final Innsyn innsyn;

    public TidslinjeTjeneste(SafSelvbetjeningTjeneste safselvbetjening, Innsyn innsyn) {
        this.safselvbetjening = safselvbetjening;
        this.innsyn = innsyn;
    }


    public List<TidslinjeHendelseDto> tidslinje(Fødselsnummer fødselsnummer, Saksnummer saksnummer) {
        var alleDokumenterFraSaf = safselvbetjening.alle(fødselsnummer, saksnummer).stream()
            .filter(journalpost -> !(INNGÅENDE_DOKUMENT.equals(journalpost.type()) && journalpost.hovedtype().erInntektsmelding()))
            .toList();
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
        if (enkelJournalpost.type().equals(EnkelJournalpost.DokumentType.UTGÅENDE_DOKUMENT)) {
            return tidslinjeHendelseTypeUtgåendeDokument(enkelJournalpost)
                .map(hendelseType -> new TidslinjeHendelseDto(
                    enkelJournalpost.mottatt(),
                    TidslinjeHendelseDto.AktørType.NAV,
                    hendelseType,
                    tilDokumenter(enkelJournalpost.dokumenter(), enkelJournalpost.journalpostId())
                ));
        } else if (enkelJournalpost.type().equals(INNGÅENDE_DOKUMENT)) {
            return tidslinjehendelsetype(enkelJournalpost, alleDokumentene)
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
        return switch (enkelJournalpost.dokumenter().stream().findFirst().orElseThrow().brevkode()) { // Alltid bare ett dokument!
            case FORELDREPENGER_ANNULLERT, FORELDREPENGER_AVSLAG, SVANGERSKAPSPENGER_OPPHØR, ENGANGSSTØNAD_INNVILGELSE, SVANGERSKAPSPENGER_AVSLAG,
                FORELDREPENGER_INNVILGELSE, ENGANGSSTØNAD_AVSLAG, FORELDREPENGER_OPPHØR, SVANGERSKAPSPENGER_INNVILGELSE,
                VEDTAK_POSITIVT_OLD, VEDTAK_AVSLAG_OLD, VEDTAK_FORELDREPENGER_OLD, VEDTAK_AVSLAG_FORELDREPENGER_OLD,
                VEDTAK_POSITIVT_OLD_MF, VEDTAK_AVSLAG_OLD_MF, VEDTAK_FORELDREPENGER_OLD_MF, VEDTAK_AVSLAG_FORELDREPENGER_OLD_MF->
                Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.VEDTAK);
            case INNHENTE_OPPLYSNINGER, INNHENTE_OPPLYSNINGER_OLD, INNHENTE_OPPLYSNINGER_OLD_MF -> Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_INNHENT_OPPLYSNINGER);
            case ETTERLYS_INNTEKTSMELDING, ETTERLYS_INNTEKTSMELDING_OLD, ETTERLYS_INNTEKTSMELDING_OLD_MF -> Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.UTGÅENDE_ETTERLYS_INNTEKTSMELDING);
            default -> {
                LOG.info("Ignorerer utgåpende journalpost med brevkode: {}", enkelJournalpost);
                yield Optional.empty();
            }
        };
    }

    private static Optional<TidslinjeHendelseDto.TidslinjeHendelseType> tidslinjehendelsetype(EnkelJournalpost enkelJournalpost, List<EnkelJournalpost> alleDokumentene) {
        if (enkelJournalpost.hovedtype().erFørstegangssøknad()) {
            return Optional.of(erNyFørstegangssøknad(enkelJournalpost, alleDokumentene) ?
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD_NY :
                TidslinjeHendelseDto.TidslinjeHendelseType.FØRSTEGANGSSØKNAD);
        } else if (enkelJournalpost.hovedtype().erEndringssøknad()) {
            return Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.ENDRINGSSØKNAD);
        } else if (enkelJournalpost.hovedtype().erVedlegg() || enkelJournalpost.hovedtype().erUttalelseOmTilbakekreving()) {
            return  Optional.of(TidslinjeHendelseDto.TidslinjeHendelseType.ETTERSENDING);
        } else {
            LOG.info("Ignorer inngående journalpost med dokumenttype: {}", enkelJournalpost);
            return Optional.empty();
        }
    }

    private static boolean erNyFørstegangssøknad(EnkelJournalpost enkelJournalpost, List<EnkelJournalpost> alleDokumentene) {
        return alleDokumentene.stream()
            .filter(j -> INNGÅENDE_DOKUMENT.equals(j.type()))
            .filter(journalpost -> journalpost.hovedtype().erFørstegangssøknad())
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
