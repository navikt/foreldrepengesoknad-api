package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AdopsjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.OmsorgsovertakelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.TerminDto;

public final class BarnBuilder {

    public static FødselBuilder fødsel(int antallBarn, LocalDate fødselsdato) {
        return new FødselBuilder(antallBarn).medFødselsdato(fødselsdato).medTermindato(fødselsdato);
    }

    public static TerminBuilder termin(int antallBarn, LocalDate termindato) {
        return new TerminBuilder(antallBarn).medTermindato(termindato).medTerminbekreftelseDato(termindato.minusMonths(1));
    }

    public static AdopsjonBuilder adopsjon(LocalDate omsorgsovertakelsesdato, boolean ektefellesBarn) {
        return new AdopsjonBuilder(1).medFødselsdatoer(List.of(LocalDate.now().minusYears(10)))
            .medAdopsjonsdato(omsorgsovertakelsesdato)
            .medAnkomstdato(omsorgsovertakelsesdato)
            .medAdopsjonAvEktefellesBarn(ektefellesBarn);
    }

    public static OmsorgsovertakelseBuilder omsorgsovertakelse(LocalDate omsorgsovertakelsedato) {
        return new OmsorgsovertakelseBuilder(1).medFødselsdatoer(List.of(LocalDate.now().minusMonths(6)))
            .medForeldreansvarsdato(omsorgsovertakelsedato);
    }

    public static class TerminBuilder {
        private int antallBarn;
        private LocalDate termindato;
        private LocalDate terminbekreftelseDato;

        public TerminBuilder(int antallBarn) {
            this.antallBarn = antallBarn;
        }

        public TerminBuilder medTermindato(LocalDate termindato) {
            this.termindato = termindato;
            return this;
        }

        public TerminBuilder medTerminbekreftelseDato(LocalDate terminbekreftelseDato) {
            this.terminbekreftelseDato = terminbekreftelseDato;
            return this;
        }

        public TerminDto build() {
            return new TerminDto(antallBarn, termindato, terminbekreftelseDato);
        }
    }

    public static class AdopsjonBuilder {
        private int antallBarn;
        private List<LocalDate> fødselsdatoer;
        private LocalDate adopsjonsdato;
        private LocalDate ankomstdato;
        private Boolean adopsjonAvEktefellesBarn;
        private Boolean søkerAdopsjonAlene;

        public AdopsjonBuilder(int antallBarn) {
            this.antallBarn = antallBarn;
        }

        public AdopsjonBuilder medFødselsdatoer(List<LocalDate> fødselsdatoer) {
            this.fødselsdatoer = fødselsdatoer;
            return this;
        }

        public AdopsjonBuilder medAdopsjonsdato(LocalDate adopsjonsdato) {
            this.adopsjonsdato = adopsjonsdato;
            return this;
        }

        public AdopsjonBuilder medAnkomstdato(LocalDate ankomstdato) {
            this.ankomstdato = ankomstdato;
            return this;
        }

        public AdopsjonBuilder medSøkerAdopsjonAlene(Boolean søkerAdopsjonAlene) {
            this.søkerAdopsjonAlene = søkerAdopsjonAlene;
            return this;
        }

        public AdopsjonBuilder medAdopsjonAvEktefellesBarn(Boolean adopsjonAvEktefellesBarn) {
            this.adopsjonAvEktefellesBarn = adopsjonAvEktefellesBarn;
            return this;
        }

        public AdopsjonDto build() {
            return new AdopsjonDto(antallBarn, fødselsdatoer, adopsjonsdato, ankomstdato, adopsjonAvEktefellesBarn, søkerAdopsjonAlene);
        }
    }

    public static class OmsorgsovertakelseBuilder {
        private int antallBarn;
        private List<LocalDate> fødselsdatoer;
        private LocalDate foreldreansvarsdato;

        public OmsorgsovertakelseBuilder(int antallBarn) {
            this.antallBarn = antallBarn;
        }

        public OmsorgsovertakelseBuilder medFødselsdatoer(List<LocalDate> fødselsdatoer) {
            this.fødselsdatoer = fødselsdatoer;
            return this;
        }

        public OmsorgsovertakelseBuilder medForeldreansvarsdato(LocalDate foreldreansvarsdato) {
            this.foreldreansvarsdato = foreldreansvarsdato;
            return this;
        }

        public OmsorgsovertakelseDto build() {
            return new OmsorgsovertakelseDto(antallBarn, fødselsdatoer, foreldreansvarsdato);
        }
    }


    public static class FødselBuilder {
        private int antallBarn;
        private LocalDate fødselsdato;
        private LocalDate termindato;

        public FødselBuilder(int antallBarn) {
            this.antallBarn = antallBarn;
        }

        public FødselBuilder medFødselsdato(LocalDate fødselsdato) {
            this.fødselsdato = fødselsdato;
            return this;
        }

        public FødselBuilder medTermindato(LocalDate termindato) {
            this.termindato = termindato;
            return this;
        }

        public FødselDto build() {
            return new FødselDto(antallBarn, fødselsdato, termindato);
        }
    }
}
