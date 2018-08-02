package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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

    public YtelseDto(Søknad søknad) {
        this.type = søknad.type;
        this.medlemsskap = new MedlemsskapDto(søknad.informasjonOmUtenlandsopphold);
        this.relasjonTilBarn = new RelasjonTilBarnDto(søknad.barn, søknad.situasjon);
        this.annenForelder = new AnnenForelderDto(søknad.annenForelder);

        if (søknad instanceof Foreldrepengesøknad) {
            Foreldrepengesøknad foreldrepengesøknad = (Foreldrepengesøknad) søknad;
            this.dekningsgrad = "GRAD100";
            this.opptjening = new OpptjeningDto(foreldrepengesøknad.søker);
            this.fordeling = new FordelingDto(foreldrepengesøknad.uttaksplan, foreldrepengesøknad.annenForelder.erInformertOmSøknaden);
            this.rettigheter = new RettigheterDto(foreldrepengesøknad.søker, foreldrepengesøknad.annenForelder);
        }
    }

    @JsonInclude(NON_NULL)
    public class AnnenForelderDto {
        public String type;
        public String id;
        public String fnr;
        public String land;
        public String fornavn;

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
    }

    @JsonInclude(NON_EMPTY)
    public class MedlemsskapDto {
        public Boolean norgeSiste12;
        public Boolean norgeNeste12;
        public Boolean fødselNorge;
        public String arbeidSiste12;
        public List<UtenlandsoppholdPeriodeDto> utenlandsopphold = new ArrayList<>();
        public List<UtenlandsoppholdPeriodeDto> framtidigUtenlandsopphold = new ArrayList<>();

        public MedlemsskapDto(Utenlandsopphold opphold) {
            this.norgeSiste12 = opphold.iNorgeSiste12Mnd;
            this.norgeNeste12 = opphold.iNorgeNeste12Mnd;
            this.fødselNorge = opphold.fødselINorge;
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
        public String land;
        public PeriodeDto varighet = new PeriodeDto();

        public UtenlandsoppholdPeriodeDto(UtenlandsoppholdPeriode periode) {
            this.land = periode.land;
            this.varighet.fom = periode.tidsperiode.startdato;
            this.varighet.tom = periode.tidsperiode.sluttdato;
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

        public RelasjonTilBarnDto(Barn barn, String situasjon) {
            this.type = type(barn.erBarnetFødt, situasjon);
            this.antallBarn = barn.antallBarn;
            this.vedlegg = barn.getAlleVedlegg();
            this.terminDato = barn.termindato;
            this.utstedtDato = barn.terminbekreftelseDato;
            this.fødselsdato = barn.fødselsdatoer;
            this.omsorgsovertakelsesdato = omsorgsovertakelsesdato(barn);
        }

        private String type(Boolean erBarnetFødt, String situasjon) {
            if (isEmpty(situasjon) || situasjon.equals("fødsel")) {
                return erBarnetFødt ? "fødsel" : "termin";
            } else {
                return situasjon;
            }
        }

        private LocalDate omsorgsovertakelsesdato(Barn barn) {
            if (barn.adopsjonsdato != null) {
                return barn.adopsjonsdato;
            } else if (barn.foreldreansvarsdato != null) {
                return barn.foreldreansvarsdato;
            } else {
                return null;
            }
        }
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
