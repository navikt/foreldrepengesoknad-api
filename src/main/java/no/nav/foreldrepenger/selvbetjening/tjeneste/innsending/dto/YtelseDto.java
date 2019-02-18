package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.AnnenForelder;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Utenlandsopphold;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.UtenlandsoppholdPeriode;

@JsonInclude(NON_NULL)
public class YtelseDto {

    public String type;
    public MedlemsskapDto medlemsskap;
    public RelasjonTilBarnDto relasjonTilBarn;

    @Override
    public String toString() {
        return "YtelseDto [type=" + type + ", medlemsskap=" + medlemsskap + ", relasjonTilBarn=" + relasjonTilBarn
                + ", annenForelder=" + annenForelder + ", dekningsgrad=" + dekningsgrad + ", opptjening=" + opptjening
                + ", fordeling=" + fordeling + ", rettigheter=" + rettigheter + "]";
    }

    public AnnenForelderDto annenForelder;
    public String dekningsgrad;
    public OpptjeningDto opptjening;
    public FordelingDto fordeling;
    public RettigheterDto rettigheter;

    public YtelseDto(Søknad søknad) {
        this.type = søknad.type;
        this.relasjonTilBarn = new RelasjonTilBarnDto(søknad.barn, søknad.situasjon);

        if (søknad.annenForelder != null) {
            this.annenForelder = new AnnenForelderDto(søknad.annenForelder);
        }

        if (!søknad.erEndringssøknad) {
            this.medlemsskap = new MedlemsskapDto(søknad.informasjonOmUtenlandsopphold);
        }

        if (søknad instanceof Foreldrepengesøknad) {
            Foreldrepengesøknad foreldrepengesøknad = (Foreldrepengesøknad) søknad;
            this.dekningsgrad = "GRAD" + foreldrepengesøknad.dekningsgrad;
            this.fordeling = new FordelingDto(foreldrepengesøknad.uttaksplan,
                    foreldrepengesøknad.annenForelder.erInformertOmSøknaden);
            this.rettigheter = new RettigheterDto(foreldrepengesøknad);

            if (!søknad.erEndringssøknad) {
                this.opptjening = new OpptjeningDto(foreldrepengesøknad.søker);
            }
        }
    }

    @JsonInclude(NON_NULL)
    public class AnnenForelderDto {
        @Override
        public String toString() {
            return "AnnenForelderDto [type=" + type + ", id=" + id + ", fnr=" + fnr + ", land=" + land + ", navn="
                    + navn + "]";
        }

        public String type;
        public String id;
        public String fnr;
        public String land;
        public String navn;

        public AnnenForelderDto(AnnenForelder annenForelder) {
            this.type = annenForelder.type();
            this.navn = type.equals("ukjent") ? null : annenForelder.fornavn + " " + annenForelder.etternavn;
            this.land = annenForelder.bostedsland;

            if (annenForelder.type().equals("norsk")) {
                this.fnr = annenForelder.fnr;
            }
            else if (annenForelder.type().equals("utenlandsk")) {
                this.id = annenForelder.fnr;
            }
        }
    }

    @JsonInclude(NON_EMPTY)
    public class MedlemsskapDto {

        @Override
        public String toString() {
            return "MedlemsskapDto{" +
                    "arbeidSiste12='" + arbeidSiste12 + '\'' +
                    ", utenlandsopphold=" + utenlandsopphold +
                    ", framtidigUtenlandsopphold=" + framtidigUtenlandsopphold +
                    '}';
        }

        public String arbeidSiste12;
        public List<UtenlandsoppholdPeriodeDto> utenlandsopphold = new ArrayList<>();
        public List<UtenlandsoppholdPeriodeDto> framtidigUtenlandsopphold = new ArrayList<>();

        public MedlemsskapDto(Utenlandsopphold opphold) {
            this.arbeidSiste12 = "IKKE_ARBEIDET";

            for (UtenlandsoppholdPeriode tidligere : opphold.tidligereOpphold) {
                utenlandsopphold.add(new UtenlandsoppholdPeriodeDto(tidligere));
            }
            for (UtenlandsoppholdPeriode senere : opphold.senereOpphold) {
                framtidigUtenlandsopphold.add(new UtenlandsoppholdPeriodeDto(senere));
            }
        }

    }

    public class UtenlandsoppholdPeriodeDto {
        @Override
        public String toString() {
            return "UtenlandsoppholdPeriodeDto [land=" + land + ", varighet=" + varighet + "]";
        }

        public String land;
        public PeriodeDto varighet = new PeriodeDto();

        public UtenlandsoppholdPeriodeDto(UtenlandsoppholdPeriode periode) {
            this.land = periode.land;
            this.varighet.fom = periode.tidsperiode.fom;
            this.varighet.tom = periode.tidsperiode.tom;
        }
    }

    @JsonInclude(NON_EMPTY)
    public class RelasjonTilBarnDto {
        public String type;
        public Integer antallBarn;
        public LocalDate terminDato;
        public List<String> vedlegg;
        public LocalDate utstedtDato;
        public List<LocalDate> fødselsdato;
        public LocalDate omsorgsovertakelsesdato;
        public LocalDate ankomstDato;

        @Override
        public String toString() {
            return "RelasjonTilBarnDto [type=" + type + ", antallBarn=" + antallBarn + ", terminDato=" + terminDato
                    + ", vedlegg=" + vedlegg + ", utstedtDato=" + utstedtDato + ", fødselsdato=" + fødselsdato
                    + ", omsorgsovertakelsesdato=" + omsorgsovertakelsesdato + ", ankomstDato=" + ankomstDato
                    + ", ektefellesBarn=" + ektefellesBarn + "]";
        }

        public Boolean ektefellesBarn;

        public RelasjonTilBarnDto(Barn barn, String situasjon) {
            this.type = type(barn.erBarnetFødt, situasjon);
            this.antallBarn = barn.antallBarn;
            this.vedlegg = barn.getAlleVedlegg();
            this.terminDato = barn.termindato;
            this.utstedtDato = barn.terminbekreftelseDato;
            this.fødselsdato = barn.fødselsdatoer;
            this.omsorgsovertakelsesdato = omsorgsovertakelsesdato(barn);
            this.ankomstDato = barn.ankomstdato;
            this.ektefellesBarn = barn.adopsjonAvEktefellesBarn;
        }

        private String type(Boolean erBarnetFødt, String situasjon) {
            if (isEmpty(situasjon) || situasjon.equals("fødsel")) {
                return erBarnetFødt ? "fødsel" : "termin";
            }
            else {
                return situasjon;
            }
        }

        private LocalDate omsorgsovertakelsesdato(Barn barn) {
            if (barn.adopsjonsdato != null) {
                return barn.adopsjonsdato;
            }
            else if (barn.foreldreansvarsdato != null) {
                return barn.foreldreansvarsdato;
            }
            else {
                return null;
            }
        }
    }

    @JsonInclude(NON_NULL)
    public class RettigheterDto {
        @Override
        public String toString() {
            return "RettigheterDto [harAnnenForelderRett=" + harAnnenForelderRett + ", harAleneOmsorgForBarnet="
                    + harAleneOmsorgForBarnet + ", datoForAleneomsorg=" + datoForAleneomsorg + "]";
        }

        public Boolean harAnnenForelderRett;
        public Boolean harAleneOmsorgForBarnet;
        public LocalDate datoForAleneomsorg;

        public RettigheterDto(Foreldrepengesøknad foreldrepengesøknad) {
            this.harAleneOmsorgForBarnet = foreldrepengesøknad.søker.erAleneOmOmsorg;
            this.harAnnenForelderRett = foreldrepengesøknad.annenForelder.harRettPåForeldrepenger;
            this.datoForAleneomsorg = foreldrepengesøknad.annenForelder.datoForAleneomsorg;
        }
    }
}
