package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.SvangerskapspengerMapper.tilArbeidsforhold;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOpptjening;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.AvtaltFerie;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.DelvisTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.HelTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.IngenTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.Tilrettelegging;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonReferanseMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.DelvisTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.HelTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.IngenTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.TilretteleggingDto;


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
            tilTilrettelegging(s, vedlegg),
            tilFerieperioder(s)
        );
    }

    private static List<AvtaltFerie> tilFerieperioder(SvangerskapspengesøknadDto s) {
        return safeStream(s.avtaltFerie()).map(af -> {
            var arbeidsforhold = tilArbeidsforhold(af.arbeidsforhold());
            return new AvtaltFerie(arbeidsforhold, af.fom(), af.tom());
        }).toList();
    }

    private static List<Tilrettelegging> tilTilrettelegging(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return s.tilrettelegging().stream()
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
}
