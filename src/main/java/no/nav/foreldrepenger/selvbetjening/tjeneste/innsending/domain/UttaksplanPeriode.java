package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.UttaksPeriode;

import java.util.List;

public class UttaksplanPeriode {
    public String type;
    public String årsak;
    public String konto;
    public String orgnr;
    public Boolean erArbeidstaker;
    public Double stillingsprosent;
    public Double samtidigUttakProsent;
    public Boolean ønskerSamtidigUttak;
    public Boolean gradert;
    public String morsAktivitetIPerioden;
    public Boolean ønskerFlerbarnsdager;

    public Tidsperiode tidsperiode;
    public String forelder;
    public List<String> vedlegg;

    public Boolean graderingInnvilget;
    public String status;

    @SuppressWarnings("unused")
    public UttaksplanPeriode() {}

    public UttaksplanPeriode(UttaksPeriode u) {
        this.tidsperiode = new Tidsperiode();
        this.tidsperiode.fom = u.getPeriode().getFom();
        this.tidsperiode.tom = u.getPeriode().getTom();

        this.ønskerSamtidigUttak = u.getSamtidigUttak();
        this.konto = u.getStønadskontotype().toString();
        this.stillingsprosent = u.getArbeidstidProsent().getProsent();

        this.graderingInnvilget = u.getGraderingInnvilget();
        this.status = u.getPeriodeResultatType().toString();
    }
}
