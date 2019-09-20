package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.UttaksPeriode;

@JsonInclude(NON_EMPTY)
public class UttaksplanPeriode {
    private String type;
    private String årsak;
    private String konto;
    private List<String> orgnumre;
    private Boolean erArbeidstaker;
    private Boolean erFrilanser;
    private Boolean erSelvstendig;
    private Double stillingsprosent;
    private Double samtidigUttakProsent;
    private Boolean ønskerSamtidigUttak;
    private Boolean gradert;
    private String morsAktivitetIPerioden;
    public Boolean ønskerFlerbarnsdager;

    private Tidsperiode tidsperiode;
    private String forelder;
    private List<String> vedlegg;

    private Boolean graderingInnvilget;
    private String status;

    public UttaksplanPeriode() {
    }

    public UttaksplanPeriode(UttaksPeriode u) {
        this.setTidsperiode(new Tidsperiode());
        this.getTidsperiode().setFom(u.getPeriode().getFom());
        this.getTidsperiode().setTom(u.getPeriode().getTom());

        this.setØnskerSamtidigUttak(u.getSamtidigUttak());
        this.setKonto(u.getStønadskontotype().toString());
        this.setStillingsprosent(u.getArbeidstidProsent().doubleValue());

        this.setGraderingInnvilget(u.getGraderingInnvilget());
        this.setStatus(u.getPeriodeResultatType().toString());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getÅrsak() {
        return årsak;
    }

    public void setÅrsak(String årsak) {
        this.årsak = årsak;
    }

    public String getKonto() {
        return konto;
    }

    public void setKonto(String konto) {
        this.konto = konto;
    }

    public List<String> getOrgnumre() {
        return orgnumre;
    }

    public void setOrgnumre(List<String> orgnumre) {
        this.orgnumre = orgnumre;
    }

    public Boolean getErArbeidstaker() {
        return erArbeidstaker;
    }

    public void setErArbeidstaker(Boolean erArbeidstaker) {
        this.erArbeidstaker = erArbeidstaker;
    }

    public Boolean getErFrilanser() {
        return erFrilanser;
    }

    public void setErFrilanser(Boolean erFrilanser) {
        this.erFrilanser = erFrilanser;
    }

    public Boolean getErSelvstendig() {
        return erSelvstendig;
    }

    public void setErSelvstendig(Boolean erSelvstendig) {
        this.erSelvstendig = erSelvstendig;
    }

    public Double getStillingsprosent() {
        return stillingsprosent;
    }

    public void setStillingsprosent(Double stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
    }

    public Double getSamtidigUttakProsent() {
        return samtidigUttakProsent;
    }

    public void setSamtidigUttakProsent(Double samtidigUttakProsent) {
        this.samtidigUttakProsent = samtidigUttakProsent;
    }

    public Boolean getØnskerSamtidigUttak() {
        return ønskerSamtidigUttak;
    }

    public void setØnskerSamtidigUttak(Boolean ønskerSamtidigUttak) {
        this.ønskerSamtidigUttak = ønskerSamtidigUttak;
    }

    public Boolean getGradert() {
        return gradert;
    }

    public void setGradert(Boolean gradert) {
        this.gradert = gradert;
    }

    public String getMorsAktivitetIPerioden() {
        return morsAktivitetIPerioden;
    }

    public void setMorsAktivitetIPerioden(String morsAktivitetIPerioden) {
        this.morsAktivitetIPerioden = morsAktivitetIPerioden;
    }

    public Tidsperiode getTidsperiode() {
        return tidsperiode;
    }

    public void setTidsperiode(Tidsperiode tidsperiode) {
        this.tidsperiode = tidsperiode;
    }

    public String getForelder() {
        return forelder;
    }

    public void setForelder(String forelder) {
        this.forelder = forelder;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public Boolean getGraderingInnvilget() {
        return graderingInnvilget;
    }

    public void setGraderingInnvilget(Boolean graderingInnvilget) {
        this.graderingInnvilget = graderingInnvilget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [type=" + getType() + ", årsak=" + getÅrsak() + ", konto=" + getKonto()
                + ", orgnumre="
                + getOrgnumre()
                + ", erArbeidstaker=" + getErArbeidstaker() + ", erFrilanser=" + getErFrilanser() + ", erSelvstendig="
                + getErSelvstendig() + ", stillingsprosent=" + getStillingsprosent() + ", samtidigUttakProsent="
                + getSamtidigUttakProsent() + ", ønskerSamtidigUttak=" + getØnskerSamtidigUttak() + ", gradert="
                + getGradert()
                + ", morsAktivitetIPerioden=" + getMorsAktivitetIPerioden() + ", ønskerFlerbarnsdager="
                + ønskerFlerbarnsdager + ", tidsperiode=" + getTidsperiode() + ", forelder=" + getForelder()
                + ", vedlegg="
                + getVedlegg() + ", graderingInnvilget=" + getGraderingInnvilget() + ", status=" + getStatus() + "]";
    }
}
