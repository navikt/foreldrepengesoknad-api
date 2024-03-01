package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.FrilanserDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.PrivatArbeidsgiverDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.SelvstendigNæringsdrivendeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.VirksomhetDto;

public class DokumentasjonReferanseMapper {

    private DokumentasjonReferanseMapper() {
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererBarn(List<VedleggDto> vedleggene) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.Type.BARN))
            .map(VedleggDto::referanse)
            .toList();
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererOpptjeningsperiode(List<VedleggDto> vedleggene, ÅpenPeriodeDto periode) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.Type.OPPTJENING))
            .filter(vedlegg -> vedlegg.dokumenterer().perioder().contains(periode))
            .map(VedleggDto::referanse)
            .toList();
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererUttaksperiode(List<VedleggDto> vedleggene, ÅpenPeriodeDto periode) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.Type.UTTAK))
            .filter(vedlegg -> vedlegg.dokumenterer().perioder().contains(periode))
            .map(VedleggDto::referanse)
            .toList();
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererTilrettelegggingAv(List<VedleggDto> vedleggene, ArbeidsforholdDto arbeidsforholdet) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.Type.TILRETTELEGGING))
            .filter(vedleggDto -> matcherArbeidsforhold(vedleggDto.dokumenterer().arbeidsforhold(), arbeidsforholdet))
            .map(VedleggDto::referanse)
            .toList();
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererTilrettelegggingAv(List<VedleggDto> vedleggene, no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto arbeidsforholdet) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.Type.BARN))
            .filter(vedleggDto -> matcherArbeidsforhold(vedleggDto.dokumenterer().arbeidsforhold(), arbeidsforholdet))
            .map(VedleggDto::referanse)
            .toList();
    }

    private static boolean matcherArbeidsforhold(ArbeidsforholdDto AFFraVedlegg, no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto AFTilrettelegging) {
        return switch (AFTilrettelegging) {
            case VirksomhetDto v -> AFFraVedlegg.type().equals(ArbeidsforholdDto.Type.VIRKSOMHET) && AFFraVedlegg.id().equals(v.id().value());
            case PrivatArbeidsgiverDto p -> AFFraVedlegg.type().equals(ArbeidsforholdDto.Type.PRIVAT) && AFFraVedlegg.id().equals(p.id().value());
            case SelvstendigNæringsdrivendeDto ignored -> AFFraVedlegg.type().equals(ArbeidsforholdDto.Type.SELVSTENDIG);
            case FrilanserDto ignored -> AFFraVedlegg.type().equals(ArbeidsforholdDto.Type.FRILANSER);
            default -> throw new IllegalStateException("Unexpected value: " + AFTilrettelegging);
        };
    }

    private static boolean matcherArbeidsforhold(ArbeidsforholdDto AFFraVedlegg, ArbeidsforholdDto AFTilrettelegging) {
        return switch (AFTilrettelegging.type()) {
            case VIRKSOMHET, PRIVAT -> AFFraVedlegg.type().equals(AFTilrettelegging.type()) && AFFraVedlegg.id().equals(AFTilrettelegging.id());
            case SELVSTENDIG, FRILANSER -> AFFraVedlegg.type().equals(AFTilrettelegging.type());
        };
    }
}
