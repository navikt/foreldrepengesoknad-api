package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.SituasjonOLD;

public class BarnBuilder {
    SituasjonOLD situasjon;
    List<LocalDate> fødselsdatoer;
    int antallBarn;
    LocalDate termindato;
    LocalDate terminbekreftelseDato;
    LocalDate adopsjonsdato;
    LocalDate ankomstdato;
    boolean adopsjonAvEktefellesBarn;
    boolean søkerAdopsjonAlene;
    LocalDate foreldreansvarsdato;

    public static BarnBuilder fødsel(int antallBarn, LocalDate fødselsdato) {
        return new BarnBuilder(antallBarn).medFødselsdatoer(List.of(fødselsdato)).medTermindato(fødselsdato).medSituasjon(SituasjonOLD.FØDSEL);
    }

    public static BarnBuilder termin(int antallBarn, LocalDate termindato) {
        return new BarnBuilder(antallBarn).medTermindato(termindato)
            .medTerminbekreftelseDato(termindato.minusMonths(1))
            .medSituasjon(SituasjonOLD.FØDSEL);
    }

    public static BarnBuilder adopsjon(LocalDate omsorgsovertakelsesdato, boolean ektefellesBarn) {
        return new BarnBuilder(1).medFødselsdatoer(List.of(LocalDate.now().minusYears(10)))
            .medAdopsjonsdato(omsorgsovertakelsesdato)
            .medAnkomstdato(omsorgsovertakelsesdato)
            .medAdopsjonAvEktefellesBarn(ektefellesBarn)
            .medSituasjon(SituasjonOLD.ADOPSJON);
    }

    public static BarnBuilder omsorgsovertakelse(LocalDate omsorgsovertakelsedato) {
        return new BarnBuilder(1).medFødselsdatoer(List.of(LocalDate.now().minusMonths(6)))
            .medAnkomstdato(omsorgsovertakelsedato)
            .medForeldreansvarsdato(omsorgsovertakelsedato)
            .medSituasjon(SituasjonOLD.OMSORGSOVERTAKELSE);
    }


    private BarnBuilder(int antallBarn) {
        this.antallBarn = antallBarn;
    }

    public BarnBuilder medSituasjon(SituasjonOLD situasjon) {
        this.situasjon = situasjon;
        return this;
    }

    public BarnBuilder medFødselsdatoer(List<LocalDate> fødselsdatoer) {
        this.fødselsdatoer = fødselsdatoer;
        return this;
    }

    public BarnBuilder medAntallBarn(int antallBarn) {
        this.antallBarn = antallBarn;
        return this;
    }

    public BarnBuilder medTermindato(LocalDate termindato) {
        this.termindato = termindato;
        return this;
    }

    public BarnBuilder medTerminbekreftelseDato(LocalDate terminbekreftelseDato) {
        this.terminbekreftelseDato = terminbekreftelseDato;
        return this;
    }

    public BarnBuilder medAdopsjonsdato(LocalDate adopsjonsdato) {
        this.adopsjonsdato = adopsjonsdato;
        return this;
    }

    public BarnBuilder medAnkomstdato(LocalDate ankomstdato) {
        this.ankomstdato = ankomstdato;
        return this;
    }

    public BarnBuilder medAdopsjonAvEktefellesBarn(boolean adopsjonAvEktefellesBarn) {
        this.adopsjonAvEktefellesBarn = adopsjonAvEktefellesBarn;
        return this;
    }

    public BarnBuilder medSøkerAdopsjonAlene(boolean søkerAdopsjonAlene) {
        this.søkerAdopsjonAlene = søkerAdopsjonAlene;
        return this;
    }

    public BarnBuilder medForeldreansvarsdato(LocalDate foreldreansvarsdato) {
        this.foreldreansvarsdato = foreldreansvarsdato;
        return this;
    }

    public BarnHelper build() {
        return new BarnHelper(
            new BarnDtoOLD(fødselsdatoer, antallBarn, termindato, terminbekreftelseDato, adopsjonsdato, ankomstdato, adopsjonAvEktefellesBarn,
                søkerAdopsjonAlene, foreldreansvarsdato), situasjon);
    }
}
