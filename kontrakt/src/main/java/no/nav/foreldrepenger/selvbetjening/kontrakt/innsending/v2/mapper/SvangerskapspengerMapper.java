package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Opptjening;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.AvtaltFerie;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.Frilanser;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.SelvstendigNæringsdrivende;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilretteleggingsbehov.Tilretteleggingbehov;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonReferanseMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FrilansDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.FrilanserDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.PrivatArbeidsgiverDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.SelvstendigNæringsdrivendeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.VirksomhetDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilretteleggingbehov.TilretteleggingbehovDto;


public final class SvangerskapspengerMapper {

    private SvangerskapspengerMapper() {
    }

    public static Søknad tilSvangerskapspengesøknad(SvangerskapspengesøknadDto s, LocalDate mottattDato) {
        var vedlegg = s.vedlegg();
        return new Søknad(
            mottattDato,
            tilSøker(s),
            tilYtelse(s, vedlegg),
            null,
            tilVedlegg(vedlegg)
        );
    }

    private static Søker tilSøker(SvangerskapspengesøknadDto s) {
        return new Søker(BrukerRolle.MOR, s.språkkode());
    }

    public static Svangerskapspenger tilYtelse(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return new Svangerskapspenger(
            s.barn().termindato(),
            s.barn().fødselsdato(),
            tilOppholdIUtlandet(s.utenlandsopphold()),
            tilOpptjening(s.egenNæring(), s.frilans(), s.andreInntekterSiste10Mnd(), vedlegg),
            tilTilretteleggingBehov(s, vedlegg),
            tilFerieperioder(s)
        );
    }

    private static List<Tilretteleggingbehov> tilTilretteleggingBehov(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return safeStream(s.tilretteleggingsbehov())
            .map(behov -> tilTilretteleggingBehov(behov, vedlegg))
            .toList();
    }

    private static Tilretteleggingbehov tilTilretteleggingBehov(TilretteleggingbehovDto tilretteleggingbehov, List<VedleggDto> vedlegg) {
        return new Tilretteleggingbehov(
            tilArbeidsforhold(tilretteleggingbehov),
            tilretteleggingbehov.behovForTilretteleggingFom(),
            tilTilrettelegging(tilretteleggingbehov.tilrettelegginger()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg, tilretteleggingbehov.arbeidsforhold()))
        );
    }

    private static Arbeidsforhold tilArbeidsforhold(TilretteleggingbehovDto tilretteleggingbehov) {
        var arbeidsforhold = tilretteleggingbehov.arbeidsforhold();
        return switch (arbeidsforhold) {
            case VirksomhetDto virksomhet -> new Virksomhet(virksomhet.id());
            case PrivatArbeidsgiverDto privat -> new PrivatArbeidsgiver(privat.id());
            case SelvstendigNæringsdrivendeDto ignore -> new SelvstendigNæringsdrivende(tilretteleggingbehov.risikofaktorer(), tilretteleggingbehov.tilretteleggingstiltak());
            case FrilanserDto ignore -> new Frilanser(tilretteleggingbehov.risikofaktorer(), tilretteleggingbehov.tilretteleggingstiltak());
            default -> throw new IllegalStateException("Utviklerfeil: Arbeidsforhold kan bare være virksomhet, privat, næring, frilans, men er " + arbeidsforhold);
        };
    }

    private static List<Tilretteleggingbehov.Tilrettelegging> tilTilrettelegging(List<TilretteleggingbehovDto.TilretteleggingDto> tilrettelegginger) {
        return safeStream(tilrettelegginger)
            .map(SvangerskapspengerMapper::tilTilrettelegging)
            .toList();
    }

    private static Tilretteleggingbehov.Tilrettelegging tilTilrettelegging(TilretteleggingbehovDto.TilretteleggingDto tilrettelegging) {
        return switch (tilrettelegging) {
            case TilretteleggingbehovDto.TilretteleggingDto.Hel hel -> new Tilretteleggingbehov.Tilrettelegging.Hel(hel.fom());
            case TilretteleggingbehovDto.TilretteleggingDto.Del del -> new Tilretteleggingbehov.Tilrettelegging.Delvis(del.fom(), del.stillingsprosent());
            case TilretteleggingbehovDto.TilretteleggingDto.Ingen ingen -> new Tilretteleggingbehov.Tilrettelegging.Ingen(ingen.fom());
            default -> throw new IllegalArgumentException("Ugyldig tilrettelegging: " + tilrettelegging);
        };
    }

    private static Opptjening tilOpptjening(NæringDto næring, FrilansDto frilans, List<AnnenInntektDto.Utlandet> utlandets, List<VedleggDto> vedlegg) {
        var annenInntekt = safeStream(utlandets).map(AnnenInntektDto.class::cast).toList();
        return CommonMapper.tilOpptjening(næring, frilans, annenInntekt, vedlegg);
    }

    private static List<AvtaltFerie> tilFerieperioder(SvangerskapspengesøknadDto s) {
        return safeStream(s.avtaltFerie()).map(af -> {
            var arbeidsforhold = tilArbeidsforhold(af.arbeidsforhold());
            return new AvtaltFerie(arbeidsforhold, af.fom(), af.tom());
        }).toList();
    }

    private static Arbeidsforhold tilArbeidsforhold(ArbeidsforholdDto arbeidsforhold) {
        if (arbeidsforhold instanceof VirksomhetDto virksomhet) {
            return tilVirksomhet(virksomhet);
        }
        if (arbeidsforhold instanceof PrivatArbeidsgiverDto privat) {
            return tilPrivatArbeidsgiver(privat);
        }
        if (arbeidsforhold instanceof SelvstendigNæringsdrivendeDto næring) {
            return tilSelvstendigNæringsdrivende(næring);
        }
        if (arbeidsforhold instanceof FrilanserDto frilans) {
            return tilFrilanser(frilans);
        }
        throw new IllegalStateException("Utviklerfeil: Arbeidsforhold kan bare være virksomhet, privat, næring, frilans, men er " + arbeidsforhold);
    }

    private static Frilanser tilFrilanser(FrilanserDto frilans) {
        return new Frilanser(frilans.risikofaktorer(), frilans.tilretteleggingstiltak());
    }

    private static SelvstendigNæringsdrivende tilSelvstendigNæringsdrivende(SelvstendigNæringsdrivendeDto næring) {
        return new SelvstendigNæringsdrivende(næring.risikofaktorer(), næring.tilretteleggingstiltak());
    }

    private static PrivatArbeidsgiver tilPrivatArbeidsgiver(PrivatArbeidsgiverDto privat) {
        return new PrivatArbeidsgiver(privat.id());
    }

    private static Virksomhet tilVirksomhet(VirksomhetDto virksomhet) {
        return new Virksomhet(virksomhet.id());
    }
}
