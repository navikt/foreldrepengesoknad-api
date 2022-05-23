package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.common.util.StringUtil.maskListe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

// TODO: Skriv om til superklasse og subklasser basert på type
@JsonInclude(NON_EMPTY)
public record UttaksplanPeriode(@Pattern(regexp = BARE_BOKSTAVER) String type,
                                Double samtidigUttakProsent,
                                Double stillingsprosent,
                                List<@Pattern(regexp = FRITEKST) String> orgnumre,
                                List<@Pattern(regexp = "^[\\p{Digit}\\p{L}]*$") String> vedlegg, // en vedleggsID
                                @Pattern(regexp = BARE_BOKSTAVER) String forelder,
                                @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$") String konto,
                                @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$") String morsAktivitetIPerioden,
                                @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$") String årsak,
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
                             String årsak, Tidsperiode tidsperiode, boolean erArbeidstaker,
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

    @Override
    public String toString() {
        return "UttaksplanPeriode{" +
            "type='" + type + '\'' +
            ", samtidigUttakProsent=" + samtidigUttakProsent +
            ", stillingsprosent=" + stillingsprosent +
            ", orgnumre=" + maskListe(orgnumre) +
            ", vedlegg=" + vedlegg +
            ", forelder='" + forelder + '\'' +
            ", konto='" + konto + '\'' +
            ", morsAktivitetIPerioden='" + morsAktivitetIPerioden + '\'' +
            ", årsak='" + årsak + '\'' +
            ", tidsperiode=" + tidsperiode +
            ", erArbeidstaker=" + erArbeidstaker +
            ", erFrilanser=" + erFrilanser +
            ", erSelvstendig=" + erSelvstendig +
            ", graderingInnvilget=" + graderingInnvilget +
            ", gradert=" + gradert +
            ", ønskerFlerbarnsdager=" + ønskerFlerbarnsdager +
            ", ønskerSamtidigUttak=" + ønskerSamtidigUttak +
            '}';
    }
}
