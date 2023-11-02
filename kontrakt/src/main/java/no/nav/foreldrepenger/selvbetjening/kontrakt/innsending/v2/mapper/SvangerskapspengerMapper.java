package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilOpptjening;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
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
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.FrilanserDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.PrivatArbeidsgiverDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.SelvstendigNæringsdrivendeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.VirksomhetDto;


public final class SvangerskapspengerMapper {

    private SvangerskapspengerMapper() {
    }

    public static Søknad tilSvangerskapspengesøknad(SvangerskapspengesøknadDto s, LocalDate mottattDato) {
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
            tilTermindato(s.barn()),
            tilFødselsdato(s.barn()),
            tilOppholdIUtlandet(s),
            tilOpptjening(s.søker()),
            tilTilrettelegging(s)
        );
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

    private static List<Tilrettelegging> tilTilrettelegging(SvangerskapspengesøknadDto s) {
        return s.tilrettelegging().stream()
            .map(SvangerskapspengerMapper::tilTilretteleggings)
            .toList();
    }

    private static Tilrettelegging tilTilretteleggings(TilretteleggingDto tilrettelegging) {
        if (tilrettelegging instanceof HelTilretteleggingDto hel) {
            return tilHelTilrettelegging(hel);
        }
        if (tilrettelegging instanceof DelvisTilretteleggingDto del) {
            return tilDelvisTilrettelegging(del);
        }
        if (tilrettelegging instanceof IngenTilretteleggingDto ingen) {
            return tilIngenTilrettelegging(ingen);
        }
        throw new IllegalStateException("Utviklerfeil: Tilrettelegging kan bare være hel, delvis eller ingen, men er " + tilrettelegging);
    }

    private static IngenTilrettelegging tilIngenTilrettelegging(IngenTilretteleggingDto tilrettelegging) {
        return new IngenTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.slutteArbeidFom(),
            tilVedleggsreferanse(tilrettelegging.vedleggsreferanser())
        );
    }

    private static DelvisTilrettelegging tilDelvisTilrettelegging(DelvisTilretteleggingDto tilrettelegging) {
        return new DelvisTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilrettelegging.stillingsprosent() != null ? ProsentAndel.valueOf(tilrettelegging.stillingsprosent()) : null,
            tilVedleggsreferanse(tilrettelegging.vedleggsreferanser())
        );
    }

    private static HelTilrettelegging tilHelTilrettelegging(HelTilretteleggingDto tilrettelegging) {
        return new HelTilrettelegging(
            tilArbeidsforhold(tilrettelegging.arbeidsforhold()),
            tilrettelegging.behovForTilretteleggingFom(),
            tilrettelegging.tilrettelagtArbeidFom(),
            tilVedleggsreferanse(tilrettelegging.vedleggsreferanser())
        );
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
