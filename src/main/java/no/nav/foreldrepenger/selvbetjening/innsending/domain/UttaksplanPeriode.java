package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

// TODO: Skriv om til superklasse og subklasser basert på type
@JsonInclude(NON_EMPTY)
public record UttaksplanPeriode(String type,
                                Double samtidigUttakProsent,
                                Double stillingsprosent,
                                List<String> orgnumre,
                                List<String> vedlegg,
                                String forelder,
                                String konto,
                                String morsAktivitetIPerioden,
                                String status,
                                String årsak,
                                Tidsperiode tidsperiode,
                                boolean erArbeidstaker,
                                boolean erFrilanser,
                                boolean erSelvstendig,
                                boolean graderingInnvilget,
                                boolean gradert,
                                boolean ønskerFlerbarnsdager,
                                boolean ønskerSamtidigUttak) {

    @JsonCreator
    public UttaksplanPeriode(String type, Double samtidigUttakProsent, Double stillingsprosent, List<String> orgnumre,
                             List<String> vedlegg, String forelder, String konto, String morsAktivitetIPerioden,
                             String status, String årsak, Tidsperiode tidsperiode, boolean erArbeidstaker,
                             boolean erFrilanser, boolean erSelvstendig, boolean graderingInnvilget, boolean gradert,
                             boolean ønskerFlerbarnsdager, boolean ønskerSamtidigUttak) {
        this.type = type;
        this.samtidigUttakProsent = samtidigUttakProsent;
        this.stillingsprosent = stillingsprosent;
        this.orgnumre = Optional.ofNullable(orgnumre).orElse(Collections.emptyList());
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(Collections.emptyList());
        this.forelder = forelder;
        this.konto = konto;
        this.morsAktivitetIPerioden = morsAktivitetIPerioden;
        this.status = status;
        this.årsak = årsak;
        this.tidsperiode = tidsperiode;
        this.erArbeidstaker = erArbeidstaker;
        this.erFrilanser = erFrilanser;
        this.erSelvstendig = erSelvstendig;
        this.graderingInnvilget = graderingInnvilget;
        this.gradert = gradert;
        this.ønskerFlerbarnsdager = ønskerFlerbarnsdager;
        this.ønskerSamtidigUttak = ønskerSamtidigUttak;
    }

}
