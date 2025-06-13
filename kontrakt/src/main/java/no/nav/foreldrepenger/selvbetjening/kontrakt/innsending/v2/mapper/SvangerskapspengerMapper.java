package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOpptjening;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.AvtaltFerie;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Tilretteleggingbehov;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.Frilanser;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.SelvstendigNæringsdrivende;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonReferanseMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.TilretteleggingbehovDto;


public final class SvangerskapspengerMapper {

    private SvangerskapspengerMapper() {
    }

    public static Søknad tilSvangerskapspengesøknad(SvangerskapspengesøknadDto s, List<VedleggDto> påkrevdeVedlegg, LocalDate mottattDato) {
        return new Søknad(mottattDato, tilSøker(s), tilYtelse(s, påkrevdeVedlegg), null, tilVedlegg(påkrevdeVedlegg));
    }

    private static Søker tilSøker(SvangerskapspengesøknadDto s) {
        return new Søker(BrukerRolle.MOR, s.språkkode());
    }

    public static Svangerskapspenger tilYtelse(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return new Svangerskapspenger(s.barn().termindato(),
            s.barn().fødselsdato(),
            tilOppholdIUtlandet(s.utenlandsopphold()),
            tilOpptjening(s.egenNæring(), s.frilans(), s.andreInntekterSiste10Mnd(), vedlegg),
            tilTilretteleggingBehov(s, vedlegg),
            tilFerieperioder(s));
    }

    private static List<Tilretteleggingbehov> tilTilretteleggingBehov(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return safeStream(s.tilretteleggingsbehov()).map(behov -> tilTilretteleggingBehov(behov, vedlegg)).toList();
    }

    private static Tilretteleggingbehov tilTilretteleggingBehov(TilretteleggingbehovDto tilretteleggingbehov, List<VedleggDto> vedlegg) {
        return new Tilretteleggingbehov(tilArbeidsforhold(tilretteleggingbehov),
            tilretteleggingbehov.behovForTilretteleggingFom(),
            tilTilrettelegging(tilretteleggingbehov.tilrettelegginger()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg,
                tilretteleggingbehov.arbeidsforhold())));
    }

    private static Arbeidsforhold tilArbeidsforhold(TilretteleggingbehovDto tilretteleggingbehov) {
        var arbeidsforhold = tilretteleggingbehov.arbeidsforhold();
        return switch (arbeidsforhold) {
            case ArbeidsforholdDto.VirksomhetDto virksomhet -> new Virksomhet(virksomhet.id());
            case ArbeidsforholdDto.PrivatArbeidsgiverDto privat -> new PrivatArbeidsgiver(privat.id());
            case ArbeidsforholdDto.SelvstendigNæringsdrivendeDto ignore ->
                new SelvstendigNæringsdrivende(tilretteleggingbehov.risikofaktorer(), tilretteleggingbehov.tilretteleggingstiltak());
            case ArbeidsforholdDto.FrilanserDto ignore ->
                new Frilanser(tilretteleggingbehov.risikofaktorer(), tilretteleggingbehov.tilretteleggingstiltak());
            default -> throw new IllegalStateException(
                "Utviklerfeil: Arbeidsforhold kan bare være virksomhet, privat, næring, frilans, men er " + arbeidsforhold);
        };
    }

    private static List<Tilretteleggingbehov.Tilrettelegging> tilTilrettelegging(List<TilretteleggingbehovDto.TilretteleggingDto> tilrettelegginger) {
        return safeStream(tilrettelegginger).map(SvangerskapspengerMapper::tilTilrettelegging).toList();
    }

    private static Tilretteleggingbehov.Tilrettelegging tilTilrettelegging(TilretteleggingbehovDto.TilretteleggingDto tilrettelegging) {
        return switch (tilrettelegging) {
            case TilretteleggingbehovDto.TilretteleggingDto.Hel(var fom) -> new Tilretteleggingbehov.Tilrettelegging.Hel(fom);
            case TilretteleggingbehovDto.TilretteleggingDto.Del(var fom, var stillingsprosent) ->
                new Tilretteleggingbehov.Tilrettelegging.Delvis(fom, stillingsprosent);
            case TilretteleggingbehovDto.TilretteleggingDto.Ingen(var fom) -> new Tilretteleggingbehov.Tilrettelegging.Ingen(fom);
            default -> throw new IllegalArgumentException("Ugyldig tilrettelegging: " + tilrettelegging);
        };
    }

    private static List<AvtaltFerie> tilFerieperioder(SvangerskapspengesøknadDto s) {
        return safeStream(s.avtaltFerie()).map(af -> {
            var arbeidsforhold = switch (af.arbeidsforhold()) {
                case ArbeidsforholdDto.VirksomhetDto virksomhet -> new Virksomhet(virksomhet.id());
                case ArbeidsforholdDto.PrivatArbeidsgiverDto privat -> new PrivatArbeidsgiver(privat.id());
                default -> throw new IllegalStateException(
                    "Utviklerfeil: Arbeidsforhold kan bare være virksomhet eller privat for avtalt ferie, men er " + af.arbeidsforhold());
            };
            return new AvtaltFerie(arbeidsforhold, af.fom(), af.tom());
        }).toList();
    }
}
