package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.*;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class YtelseDto {

    public String type;
    public MedlemsskapDto medlemsskap;
    public RelasjonTilBarnDto relasjonTilBarn;
    public AnnenForelderDto annenForelder;
    public String dekningsgrad;
    public OpptjeningDto opptjening;
    public FordelingDto fordeling;
    public RettigheterDto rettigheter;

    public YtelseDto(String type, Utenlandsopphold utenlandsopphold, Barn barn, AnnenForelder annenForelder) {
        this.type = type;
        this.medlemsskap = new MedlemsskapDto(utenlandsopphold);
        this.relasjonTilBarn = new RelasjonTilBarnDto(barn);
        this.annenForelder = new AnnenForelderDto(annenForelder);
    }

    public YtelseDto(Foreldrepengesøknad søknad) {
        this(søknad.type, søknad.informasjonOmUtenlandsopphold, søknad.barn, søknad.annenForelder);

        this.dekningsgrad = "GRAD100";
        this.opptjening = new OpptjeningDto(søknad.søker);
        this.fordeling = new FordelingDto(søknad.uttaksplan, søknad.annenForelder.erInformertOmSøknaden);
        this.rettigheter = new RettigheterDto(søknad.søker, søknad.annenForelder);
    }

    @JsonInclude(NON_NULL)
    public class AnnenForelderDto {
        public AnnenForelderDto(AnnenForelder annenForelder) {
            this.type = annenForelder.type();
            this.fornavn = annenForelder.navn;
            this.land = annenForelder.bostedsland;

            if (annenForelder.type().equals("norsk")) {
                this.fnr = annenForelder.fnr;
            }
            else if (annenForelder.type().equals("utenlandsk")) {
                this.id = annenForelder.fnr;
            }
        }

        public String type;
        public String id;
        public String fnr;
        public String land;
        public String fornavn;
    }

    @JsonInclude(NON_EMPTY)
    public class MedlemsskapDto {
        public MedlemsskapDto(Utenlandsopphold utenlandsopphold) {
            this.norgeSiste12 = utenlandsopphold.iNorgeSiste12Mnd;
            this.norgeNeste12 = utenlandsopphold.iNorgeNeste12Mnd;
            this.fødselNorge = utenlandsopphold.fødselINorge;
            this.utenlandsopphold = utenlandsopphold.tidligereOpphold;
            this.framtidigUtenlandsopphold = utenlandsopphold.senereOpphold;
            this.arbeidSiste12 = "IKKE_ARBEIDET";
        }

        public Boolean norgeSiste12;
        public Boolean norgeNeste12;
        public Boolean fødselNorge;
        public String arbeidSiste12;
        public List<UtenlandsoppholdPeriode> utenlandsopphold;
        public List<UtenlandsoppholdPeriode> framtidigUtenlandsopphold;
    }

    @JsonInclude(NON_EMPTY)
    public class RelasjonTilBarnDto {
        public RelasjonTilBarnDto(Barn barn) {
            this.type = barn.erBarnetFødt ? "fødsel" : "termin";
            this.antallBarn = barn.antallBarn;
            this.terminDato = barn.termindato;
            this.utstedtDato = barn.terminbekreftelseDato;
            this.fødselsdato = barn.fødselsdatoer;
        }

        public String type;
        public Integer antallBarn;
        public LocalDate terminDato;
        public LocalDate utstedtDato;
        public List<LocalDate> fødselsdato;
    }

    @JsonInclude(NON_NULL)
    public class RettigheterDto {
        public Boolean harAnnenForelderRett;
        public Boolean harAleneOmsorgForBarnet;

        public RettigheterDto(Søker søker, AnnenForelder annenForelder) {
            this.harAleneOmsorgForBarnet = søker.erAleneOmOmsorg;
            this.harAnnenForelderRett = annenForelder.harRettPåForeldrepenger;
        }
    }
}
