package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;
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

    private static boolean matcherArbeidsforhold(ArbeidsforholdDto arbeidsforholdVedlegg, ArbeidsforholdDto arbeidsforholdSøknad) {
        return switch (arbeidsforholdSøknad) {
            case VirksomhetDto v -> arbeidsforholdVedlegg instanceof VirksomhetDto afVedlegg && afVedlegg.id().equals(v.id());
            case PrivatArbeidsgiverDto p -> arbeidsforholdVedlegg instanceof PrivatArbeidsgiverDto afVedlegg && afVedlegg.id().equals(p.id());
            case SelvstendigNæringsdrivendeDto ignored -> arbeidsforholdVedlegg instanceof SelvstendigNæringsdrivendeDto;
            case FrilanserDto ignored -> arbeidsforholdVedlegg instanceof FrilanserDto;
            default -> throw new IllegalStateException("Unexpected value: " + arbeidsforholdSøknad);
        };
    }
}
