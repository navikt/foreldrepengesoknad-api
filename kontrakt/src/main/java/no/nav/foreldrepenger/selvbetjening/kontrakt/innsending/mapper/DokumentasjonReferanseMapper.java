package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.ArbeidsforholdDto;

public class DokumentasjonReferanseMapper {

    private DokumentasjonReferanseMapper() {
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererBarn(List<VedleggDto> vedleggene) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.DokumentererType.BARN))
            .map(VedleggDto::referanse)
            .toList();
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererOpptjeningsperiode(List<VedleggDto> vedleggene, ÅpenPeriodeDtoOLD periode) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.DokumentererType.OPPTJENING))
            .filter(vedlegg -> vedlegg.dokumenterer().perioder().contains(periode))
            .map(VedleggDto::referanse)
            .toList();
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererUttaksperiode(List<VedleggDto> vedleggene, ÅpenPeriodeDtoOLD periode) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.DokumentererType.UTTAK))
            .filter(vedlegg -> vedlegg.dokumenterer().perioder().contains(periode))
            .map(VedleggDto::referanse)
            .toList();
    }

    public static List<VedleggReferanse> dokumentasjonSomDokumentererTilrettelegggingAv(List<VedleggDto> vedleggene,
                                                                                        ArbeidsforholdDto arbeidsforholdet) {
        return vedleggene.stream()
            .filter(vedlegg -> vedlegg.dokumenterer().type().equals(VedleggDto.Dokumenterer.DokumentererType.TILRETTELEGGING))
            .filter(vedleggDto -> matcherArbeidsforhold(vedleggDto.dokumenterer().arbeidsforhold(), arbeidsforholdet))
            .map(VedleggDto::referanse)
            .toList();
    }

    private static boolean matcherArbeidsforhold(ArbeidsforholdDto arbeidsforholdVedlegg, ArbeidsforholdDto arbeidsforholdSøknad) {
        return switch (arbeidsforholdSøknad) {
            case ArbeidsforholdDto.VirksomhetDto v ->
                arbeidsforholdVedlegg instanceof ArbeidsforholdDto.VirksomhetDto afVedlegg && afVedlegg.id().equals(v.id());
            case ArbeidsforholdDto.PrivatArbeidsgiverDto p ->
                arbeidsforholdVedlegg instanceof ArbeidsforholdDto.PrivatArbeidsgiverDto afVedlegg && afVedlegg.id().equals(p.id());
            case ArbeidsforholdDto.SelvstendigNæringsdrivendeDto ignored ->
                arbeidsforholdVedlegg instanceof ArbeidsforholdDto.SelvstendigNæringsdrivendeDto;
            case ArbeidsforholdDto.FrilanserDto ignored -> arbeidsforholdVedlegg instanceof ArbeidsforholdDto.FrilanserDto;
            default -> throw new IllegalStateException("Unexpected value: " + arbeidsforholdSøknad);
        };
    }
}
