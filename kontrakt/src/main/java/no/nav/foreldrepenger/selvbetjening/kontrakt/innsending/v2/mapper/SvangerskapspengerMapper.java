package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilAnnenOpptjening;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilEgenNæring;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilFrilans;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilUtenlandsArbeidsforhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SvangerskapspengerMapper.tilArbeidsforhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Opptjening;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.AvtaltFerie;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.DelvisTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.HelTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.IngenTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.Tilrettelegging;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonReferanseMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AdopsjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.OmsorgsovertakelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.TerminDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.DelvisTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.HelTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.IngenTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.TilretteleggingDto;


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
            tilTermindato(s.barn()),
            tilFødselsdato(s.barn()),
            tilOppholdIUtlandet(s),
            tilOpptjening(s, vedlegg),
            tilTilrettelegging(s, vedlegg),
            tilFerieperioder(s)
        );
    }


    private static List<AvtaltFerie> tilFerieperioder(SvangerskapspengesøknadDto s) {
        return safeStream(s.avtalteFerieperioder()).map(af -> {
            var arbeidsforhold = tilArbeidsforhold(af.arbeidsforholdDto());
            return new AvtaltFerie(arbeidsforhold, af.fom(), af.tom());
        }).toList();
    }
    private static LocalDate tilTermindato(BarnDto barn) {
        if (barn instanceof FødselDto fødsel) {
            return fødsel.termindato();
        }
        if (barn instanceof TerminDto termin) {
            return termin.termindato();
        }
        return null;
    }

    private static LocalDate tilFødselsdato(BarnDto barn) {
        if (barn instanceof FødselDto fødsel) {
            return fødsel.fødselsdato();
        }
        if (barn instanceof AdopsjonDto adopsjon) {
            return adopsjon.fødselsdatoer().stream()
                .findFirst()
                .orElse(null);
        }
        if (barn instanceof OmsorgsovertakelseDto omsorgsovertakelse) {
            return omsorgsovertakelse.fødselsdatoer().stream()
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    private static List<Tilrettelegging> tilTilrettelegging(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return s.tilretteleggingsbehov().stream()
            .map(tilrettelegging -> tilTilretteleggings(tilrettelegging, vedlegg))
            .toList();
    }

    private static Tilrettelegging tilTilretteleggings(TilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        if (tilrettelegging instanceof HelTilretteleggingDto hel) {
            return tilHelTilrettelegging(hel, vedlegg);
        }
        if (tilrettelegging instanceof DelvisTilretteleggingDto del) {
            return tilDelvisTilrettelegging(del, vedlegg);
        }
        if (tilrettelegging instanceof IngenTilretteleggingDto ingen) {
            return tilIngenTilrettelegging(ingen, vedlegg);
        }
        throw new IllegalStateException("Utviklerfeil: Tilrettelegging kan bare være hel, delvis eller ingen, men er " + tilrettelegging);
    }

    private static IngenTilrettelegging tilIngenTilrettelegging(IngenTilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        return new IngenTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.slutteArbeidFom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg, tilrettelegging.arbeidsforhold()))
        );
    }

    private static DelvisTilrettelegging tilDelvisTilrettelegging(DelvisTilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        return new DelvisTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            ProsentAndel.valueOf(tilrettelegging.stillingsprosent()),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg, tilrettelegging.arbeidsforhold()))
        );
    }

    private static HelTilrettelegging tilHelTilrettelegging(HelTilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        return new HelTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg, tilrettelegging.arbeidsforhold()))
        );
    }

    public static Opptjening tilOpptjening(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return new Opptjening(
            tilUtenlandsArbeidsforhold(s.andreInntekterSiste10Mnd(), vedlegg),
            tilEgenNæring(s.selvstendigNæringsdrivendeInformasjon(), vedlegg),
            tilAnnenOpptjening(s.andreInntekterSiste10Mnd(), vedlegg),
            tilFrilans(s.frilansInformasjon())
        );
    }
}
