package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.AnnenForelder;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Utenlandsopphold;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.UtenlandsoppholdPeriode;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilrettelegging.Arbeidsforhold;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilrettelegging.Tilrettelegging;

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
    public List<TilretteleggingDto> tilrettelegging;

    public LocalDate termindato;
    public LocalDate fødselsdato;

    public YtelseDto(Søknad søknad) {
        this.type = søknad.type;

        if (søknad instanceof Foreldrepengesøknad) {
            Foreldrepengesøknad foreldrepengesøknad = (Foreldrepengesøknad) søknad;
            this.relasjonTilBarn = new RelasjonTilBarnDto(søknad.barn, søknad.situasjon);
            this.dekningsgrad = "GRAD" + foreldrepengesøknad.dekningsgrad;
            this.fordeling = new FordelingDto(foreldrepengesøknad.uttaksplan,
                    foreldrepengesøknad.annenForelder.erInformertOmSøknaden);
            this.rettigheter = new RettigheterDto(foreldrepengesøknad);
        }

        if (søknad instanceof Engangsstønad) {
            this.relasjonTilBarn = new RelasjonTilBarnDto(søknad.barn, søknad.situasjon);
        }

        if (søknad instanceof Svangerskapspengesøknad) {
            Svangerskapspengesøknad svangerskapspengesøknad = (Svangerskapspengesøknad) søknad;
            this.termindato = svangerskapspengesøknad.barn.termindato;
            if (isNotEmpty(svangerskapspengesøknad.barn.fødselsdatoer)) {
                this.fødselsdato = svangerskapspengesøknad.barn.fødselsdatoer.get(0);
            }
            this.tilrettelegging = svangerskapspengesøknad.tilrettelegging.stream().map(TilretteleggingDto::new)
                    .collect(toList());
        }

        if (søknad.annenForelder != null) {
            this.annenForelder = new AnnenForelderDto(søknad.annenForelder);
        }

        if (!søknad.erEndringssøknad) {
            this.medlemsskap = new MedlemsskapDto(søknad.informasjonOmUtenlandsopphold);
            if (søknad instanceof Foreldrepengesøknad || søknad instanceof Svangerskapspengesøknad) {
                this.opptjening = new OpptjeningDto(søknad.søker);
            }
        }
    }

    @JsonInclude(NON_NULL)
    public class AnnenForelderDto {

        public String type;
        public String id;
        public String fnr;
        public String land;
        public String navn;

        public AnnenForelderDto(AnnenForelder annenForelder) {
            this.type = annenForelder.type();
            this.navn = type.equals("ukjent") ? null : navn(annenForelder);
            this.land = annenForelder.bostedsland;

            if (annenForelder.type().equals("norsk")) {
                this.fnr = annenForelder.fnr;
            }
            else if (annenForelder.type().equals("utenlandsk")) {
                this.id = annenForelder.fnr;
            }
        }

        private String navn(AnnenForelder annenForelder) {
            return isNotBlank(annenForelder.navn) ? annenForelder.navn
                    : annenForelder.fornavn + " " + annenForelder.etternavn;
        }
    }

    @JsonInclude(NON_EMPTY)
    public class MedlemsskapDto {

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

        public Boolean harAnnenForelderRett;
        public Boolean harAleneOmsorgForBarnet;
        public LocalDate datoForAleneomsorg;

        public RettigheterDto(Foreldrepengesøknad foreldrepengesøknad) {
            this.harAleneOmsorgForBarnet = foreldrepengesøknad.søker.erAleneOmOmsorg;
            this.harAnnenForelderRett = foreldrepengesøknad.annenForelder.harRettPåForeldrepenger;
            this.datoForAleneomsorg = foreldrepengesøknad.annenForelder.datoForAleneomsorg;
        }
    }

    @JsonInclude(NON_NULL)
    public class TilretteleggingDto {
        public String type;
        public LocalDate behovForTilretteleggingFom;
        public LocalDate tilrettelagtArbeidFom;
        public LocalDate slutteArbeidFom;
        public Double stillingsprosent;
        public ArbeidsforholdDto arbeidsforhold;
        public List<String> vedlegg;

        public TilretteleggingDto(Tilrettelegging tilrettelegging) {
            this.type = tilrettelegging.type;
            this.arbeidsforhold = new ArbeidsforholdDto(tilrettelegging.arbeidsforhold);
            this.behovForTilretteleggingFom = tilrettelegging.behovForTilretteleggingFom;
            this.tilrettelagtArbeidFom = tilrettelegging.tilrettelagtArbeidFom;
            this.stillingsprosent = tilrettelegging.stillingsprosent;
            this.slutteArbeidFom = tilrettelegging.slutteArbeidFom;
            this.vedlegg = tilrettelegging.vedlegg;
        }
    }

    @JsonInclude(NON_NULL)
    public class ArbeidsforholdDto {
        public String type;
        public String orgnr;
        public String fnr;

        public ArbeidsforholdDto(Arbeidsforhold arbeidsforhold) {
            this.type = arbeidsforhold.type;

            if (arbeidsforhold.type.equals("virksomhet")) {
                this.orgnr = arbeidsforhold.id;
            }
            else {
                this.fnr = arbeidsforhold.id;
            }

        }
    }
}
