package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.StønadskontoType;

public class PeriodeAktivitet {

    private final String arbeidsforholdId;
    private final ProsentAndel arbeidstidProsent;
    private final AvslagsÅrsak avslagsÅrsak;
    private final Boolean gradering;
    private final Integer trekkDager;
    private final StønadskontoType trekkonto;
    private final ProsentAndel utbetalingProsent;
    private final ArbeidType arbeidType;
    private final String virksomhet;

    public PeriodeAktivitet(String arbeidsforholdId, ProsentAndel arbeidstidProsent, AvslagsÅrsak avslagsÅrsak,
            Boolean gradering, Integer trekkDager, StønadskontoType trekkonto, ProsentAndel utbetalingProsent,
            ArbeidType arbeidType, String virksomhet) {
        this.arbeidsforholdId = arbeidsforholdId;
        this.arbeidstidProsent = arbeidstidProsent;
        this.avslagsÅrsak = avslagsÅrsak;
        this.gradering = gradering;
        this.trekkDager = trekkDager;
        this.trekkonto = trekkonto;
        this.utbetalingProsent = utbetalingProsent;
        this.arbeidType = arbeidType;
        this.virksomhet = virksomhet;
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public ProsentAndel getArbeidstidProsent() {
        return arbeidstidProsent;
    }

    public AvslagsÅrsak getAvslagsÅrsak() {
        return avslagsÅrsak;
    }

    public Boolean getGradering() {
        return gradering;
    }

    public Integer getTrekkDager() {
        return trekkDager;
    }

    public StønadskontoType getTrekkonto() {
        return trekkonto;
    }

    public ProsentAndel getUtbetalingProsent() {
        return utbetalingProsent;
    }

    public ArbeidType getArbeidType() {
        return arbeidType;
    }

    public String getVirksomhet() {
        return virksomhet;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [arbeidsforholdId=" + arbeidsforholdId + ", arbeidstidProsent="
                + arbeidstidProsent
                + ", avslagsÅrsak=" + avslagsÅrsak + ", gradering=" + gradering + ", trekkDager=" + trekkDager
                + ", trekkonto=" + trekkonto + ", utbetalingProsent=" + utbetalingProsent + ", arbeidType=" + arbeidType
                + ", virksomhet=" + virksomhet + "]";
    }

}
