package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilMedlemskap;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilOpptjening;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

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
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SvangerskapspengesøknadFrontend;

final class SvangerskapspengerMapper {

    private SvangerskapspengerMapper() {
    }

    static Søknad tilSvangerskapspengesøknad(SvangerskapspengesøknadFrontend s) {
        return new Søknad(
            LocalDate.now(),
            tilSøker(s),
            tilYtelse(s),
            s.getTilleggsopplysninger(),
            new ArrayList<>() // Settes av InnsendingConnection etter logging
        );
    }

    private static Søker tilSøker(SvangerskapspengesøknadFrontend s) {
        var søker = s.getSøker();
        if (søker == null) {
            throw new IllegalStateException("Kan ikke ha tom søkerobjekt");
        }
        return new Søker(BrukerRolle.MOR, søker.språkkode());
    }

    public static Svangerskapspenger tilYtelse(SvangerskapspengesøknadFrontend s) {
        return new Svangerskapspenger(
            s.getBarn().termindato(),
            tilFødselsdato(s),
            Boolean.FALSE.equals(s.getErEndringssøknad()) ? tilMedlemskap(s) : null,
            Boolean.FALSE.equals(s.getErEndringssøknad()) ? tilOpptjening(s) : null,
            tilTilrettelegging(s)
        );
    }

    private static LocalDate tilFødselsdato(SvangerskapspengesøknadFrontend s) {
        if (!CollectionUtils.isEmpty(s.getBarn().fødselsdatoer())) {
            return s.getBarn().fødselsdatoer().get(0);
        }
        return null;
    }

    private static List<Tilrettelegging> tilTilrettelegging(SvangerskapspengesøknadFrontend s) {
        return s.getTilrettelegging().stream()
            .map(SvangerskapspengerMapper::tilTilretteleggings)
            .toList();
    }

    private static Tilrettelegging tilTilretteleggings(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging tilrettelegging) {
        return switch (tilrettelegging.type()) {
            case "hel" -> tilHelTilrettelegging(tilrettelegging);
            case "delvis" -> tilDelvisTilrettelegging(tilrettelegging);
            case "ingen" -> tilIngenTilrettelegging(tilrettelegging);
            default -> throw new IllegalStateException("Ukjent tilretteleggingstype: " + tilrettelegging.type());
        };
    }

    private static IngenTilrettelegging tilIngenTilrettelegging(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging tilrettelegging) {
        return new IngenTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.slutteArbeidFom(),
            tilrettelegging.vedlegg()
        );
    }

    private static DelvisTilrettelegging tilDelvisTilrettelegging(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging tilrettelegging) {
        return new DelvisTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilrettelegging.stillingsprosent() != null ? ProsentAndel.valueOf(tilrettelegging.stillingsprosent()) : null,
            tilrettelegging.vedlegg()
        );
    }

    private static HelTilrettelegging tilHelTilrettelegging(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging tilrettelegging) {
        return new HelTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilrettelegging.vedlegg()
        );
    }

    private static Arbeidsforhold tilArbeidsforhold(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Arbeidsforhold arbeidsforhold) {
        return switch (arbeidsforhold.type()) {
            case "virksomhet" -> tilVirksomhet(arbeidsforhold);
            case "privat" -> tilPrivatArbeidsgiver(arbeidsforhold);
            case "selvstendig" -> tilSelvstendigNæringsdrivende(arbeidsforhold);
            case "frilanser" -> tilFrilanser(arbeidsforhold);
            default -> throw new IllegalStateException("Ikke støttet arbeidsforholdtype: " + arbeidsforhold.type());
        };
    }

    private static Frilanser tilFrilanser(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Arbeidsforhold arbeidsforhold) {
        return new Frilanser(arbeidsforhold.risikofaktorer(), arbeidsforhold.tilretteleggingstiltak());
    }

    private static SelvstendigNæringsdrivende tilSelvstendigNæringsdrivende(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Arbeidsforhold arbeidsforhold) {
        return new SelvstendigNæringsdrivende(arbeidsforhold.risikofaktorer(), arbeidsforhold.tilretteleggingstiltak());
    }

    private static PrivatArbeidsgiver tilPrivatArbeidsgiver(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Arbeidsforhold arbeidsforhold) {
        return new PrivatArbeidsgiver(new Fødselsnummer(arbeidsforhold.id()));
    }

    private static Virksomhet tilVirksomhet(no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Arbeidsforhold arbeidsforhold) {
        return new Virksomhet(Orgnummer.valueOf(arbeidsforhold.id()));
    }
}
