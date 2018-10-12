package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.util.List;

public class UttaksplanPeriode {
    public String type;
    public String årsak;
    public String konto;
    public String orgnr;
    public Boolean erArbeidstaker;
    public String stillingsprosent;
    public Boolean ønskerSamtidigUttak;
    public Boolean gradert;
    public String morsAktivitetIPerioden;

    public Tidsperiode tidsperiode;
    public String forelder;
    public List<String> vedlegg;

    @Override
    public String toString() {
        return "UttaksplanPeriode{" +
                "type='" + type + '\'' +
                ", årsak='" + årsak + '\'' +
                ", konto='" + konto + '\'' +
                ", orgnr='" + orgnr + '\'' +
                ", erArbeidstaker=" + erArbeidstaker +
                ", stillingsprosent='" + stillingsprosent + '\'' +
                ", ønskerSamtidigUttak=" + ønskerSamtidigUttak +
                ", gradert=" + gradert +
                ", morsAktivitetIPerioden='" + morsAktivitetIPerioden + '\'' +
                ", tidsperiode=" + tidsperiode +
                ", forelder='" + forelder + '\'' +
                ", vedlegg=" + vedlegg +
                '}';
    }
}
