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
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.TilretteleggingDto;

// TODO: Gå gjennom verdier som er Nullable!
final class SvangerskapspengerMapper {

    private SvangerskapspengerMapper() {
    }

    static Søknad tilSvangerskapspengesøknadVedleggUtenInnhold(SvangerskapspengesøknadDto s, LocalDate mottattDato) {
        return new Søknad(
            mottattDato,
            tilSøker(s),
            tilYtelse(s),
            null,
            tilVedlegg(s.vedlegg())
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

    public static Svangerskapspenger tilYtelse(SvangerskapspengesøknadDto s) {
        return new Svangerskapspenger(
            s.barn().termindato(),
            tilFødselsdato(s),
            tilOppholdIUtlandet(s),
            tilOpptjening(s),
            tilTilrettelegging(s)
        );
    }

    private static LocalDate tilFødselsdato(SvangerskapspengesøknadDto s) {
        return s.barn().fødselsdatoer().stream()
            .findFirst()
            .orElse(null);
    }

    private static List<Tilrettelegging> tilTilrettelegging(SvangerskapspengesøknadDto s) {
        return s.tilrettelegging().stream()
            .map(SvangerskapspengerMapper::tilTilretteleggings)
            .toList();
    }

    private static Tilrettelegging tilTilretteleggings(TilretteleggingDto tilrettelegging) {
        return switch (tilrettelegging.type()) {
            case HEL -> tilHelTilrettelegging(tilrettelegging);
            case DELVIS -> tilDelvisTilrettelegging(tilrettelegging);
            case INGEN -> tilIngenTilrettelegging(tilrettelegging);
        };
    }

    private static IngenTilrettelegging tilIngenTilrettelegging(TilretteleggingDto tilrettelegging) {
        return new IngenTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.slutteArbeidFom(),
            tilVedleggsreferanse(tilrettelegging.vedlegg())
        );
    }

    private static DelvisTilrettelegging tilDelvisTilrettelegging(TilretteleggingDto tilrettelegging) {
        return new DelvisTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilrettelegging.stillingsprosent() != null ? ProsentAndel.valueOf(tilrettelegging.stillingsprosent()) : null,
            tilVedleggsreferanse(tilrettelegging.vedlegg())
        );
    }

    private static HelTilrettelegging tilHelTilrettelegging(TilretteleggingDto tilrettelegging) {
        return new HelTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilVedleggsreferanse(tilrettelegging.vedlegg())
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
