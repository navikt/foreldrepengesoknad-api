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

    public YtelseDto(String type, Utenlandsopphold utenlandsopphold, Barn barn, AnnenForelder annenForelder) {
        this.type = type;
        this.medlemsskap = new MedlemsskapDto(utenlandsopphold);
        this.relasjonTilBarn = new RelasjonTilBarnDto(barn);
        this.annenForelder = new AnnenForelderDto(annenForelder);
    }

    public YtelseDto(String type, Utenlandsopphold utenlandsopphold, Barn barn, AnnenForelder annenForelder, List<UttaksplanPeriode> uttaksplan) {
        this(type, utenlandsopphold, barn, annenForelder);

        this.dekningsgrad = type.equals("foreldrepenger") ? "GRAD100" : null; // TODO FIX
        this.opptjening = new OpptjeningDto();
        this.fordeling = new FordelingDto(uttaksplan);
    }

    @JsonInclude(NON_NULL)
    public class AnnenForelderDto {
        public AnnenForelderDto(AnnenForelder annenForelder) {
            this.type = annenForelder.type();
            this.fornavn = annenForelder.navn;
            this.land = annenForelder.bostedsland;

            if (annenForelder.type().equals("norsk")) {
                this.fnr = annenForelder.fnr;
            } else if (annenForelder.type().equals("utenlandsk")) {
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
            this.utenlandsopphold = utenlandsopphold.tidligerePerioder;
            this.framtidigUtenlandsopphold = utenlandsopphold.senerePerioder;
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

}
