package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.Svangerskapspenger;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.DelvisTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.HelTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.IngenTilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.Tilrettelegging;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Frilanser;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.SelvstendigNæringsdrivende;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.TilretteleggingDto;

// TODO: Gå gjennom verdier som er Nullable!
final class SvangerskapspengerMapper {

    private SvangerskapspengerMapper() {
    }

    static Søknad tilSvangerskapspengesøknadVedleggUtenInnhold(SvangerskapspengesøknadDto s, LocalDate mottattDato) {
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
        var søker = s.søker();
        if (søker == null) {
            throw new IllegalStateException("Kan ikke ha tom søkerobjekt");
        }
        if (søker.rolle() != BrukerRolle.MOR) {
            throw new IllegalStateException("Forventet at søker var mor, men var " + søker.rolle());
        }
        return new Søker(BrukerRolle.MOR, søker.språkkode());
    }

    public static Svangerskapspenger tilYtelse(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return new Svangerskapspenger(
            s.barn().termindato(),
            tilFødselsdato(s),
            tilOppholdIUtlandet(s),
            tilOpptjening(s.søker(), vedlegg),
            tilTilrettelegging(s, vedlegg)
        );
    }

    private static LocalDate tilFødselsdato(SvangerskapspengesøknadDto s) {
        return s.barn().fødselsdatoer().stream()
            .findFirst()
            .orElse(null);
    }

    private static List<Tilrettelegging> tilTilrettelegging(SvangerskapspengesøknadDto s, List<VedleggDto> vedlegg) {
        return s.tilrettelegging().stream()
            .map(tilrettelegging -> tilTilretteleggings(tilrettelegging, vedlegg))
            .toList();
    }

    private static Tilrettelegging tilTilretteleggings(TilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        return switch (tilrettelegging.type()) {
            case HEL -> tilHelTilrettelegging(tilrettelegging, vedlegg);
            case DELVIS -> tilDelvisTilrettelegging(tilrettelegging, vedlegg);
            case INGEN -> tilIngenTilrettelegging(tilrettelegging, vedlegg);
        };
    }

    private static IngenTilrettelegging tilIngenTilrettelegging(TilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        return new IngenTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.slutteArbeidFom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg, tilrettelegging.arbeidsforhold()))
        );
    }

    private static DelvisTilrettelegging tilDelvisTilrettelegging(TilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        return new DelvisTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilrettelegging.stillingsprosent() != null ? ProsentAndel.valueOf(tilrettelegging.stillingsprosent()) : null,
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg, tilrettelegging.arbeidsforhold()))
        );
    }

    private static HelTilrettelegging tilHelTilrettelegging(TilretteleggingDto tilrettelegging, List<VedleggDto> vedlegg) {
        return new HelTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererTilrettelegggingAv(vedlegg, tilrettelegging.arbeidsforhold()))
        );
    }

    public static Arbeidsforhold tilArbeidsforhold(ArbeidsforholdDto arbeidsforhold) {
        return switch (arbeidsforhold.type()) {
            case VIRKSOMHET -> tilVirksomhet(arbeidsforhold);
            case PRIVAT -> tilPrivatArbeidsgiver(arbeidsforhold);
            case SELVSTENDIG -> tilSelvstendigNæringsdrivende(arbeidsforhold);
            case FRILANSER -> tilFrilanser(arbeidsforhold);
        };
    }

    private static Frilanser tilFrilanser(ArbeidsforholdDto arbeidsforhold) {
        return new Frilanser(arbeidsforhold.risikofaktorer(), arbeidsforhold.tilretteleggingstiltak());
    }

    private static SelvstendigNæringsdrivende tilSelvstendigNæringsdrivende(ArbeidsforholdDto arbeidsforhold) {
        return new SelvstendigNæringsdrivende(arbeidsforhold.risikofaktorer(), arbeidsforhold.tilretteleggingstiltak());
    }

    private static PrivatArbeidsgiver tilPrivatArbeidsgiver(ArbeidsforholdDto arbeidsforhold) {
        return new PrivatArbeidsgiver(new Fødselsnummer(arbeidsforhold.id()));
    }

    private static Virksomhet tilVirksomhet(ArbeidsforholdDto arbeidsforhold) {
        return new Virksomhet(new Orgnummer(arbeidsforhold.id()));
    }
}
