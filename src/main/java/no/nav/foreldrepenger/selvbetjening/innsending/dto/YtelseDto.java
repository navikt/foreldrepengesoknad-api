package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.AnnenForelder;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Utenlandsopphold;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UtenlandsoppholdPeriode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Arbeidsforhold;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging.Tilrettelegging;

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
        this.type = søknad.getType();

        if (søknad instanceof Foreldrepengesøknad) {
            Foreldrepengesøknad foreldrepengesøknad = (Foreldrepengesøknad) søknad;
            this.relasjonTilBarn = new RelasjonTilBarnDto(søknad.getBarn(), søknad.getSituasjon());
            this.dekningsgrad = "GRAD" + foreldrepengesøknad.getDekningsgrad();
            this.fordeling = new FordelingDto(foreldrepengesøknad.getUttaksplan(),
                    foreldrepengesøknad.getAnnenForelder().getErInformertOmSøknaden());
            this.rettigheter = new RettigheterDto(foreldrepengesøknad);
        }

        if (søknad instanceof Engangsstønad) {
            this.relasjonTilBarn = new RelasjonTilBarnDto(søknad.getBarn(), søknad.getSituasjon());
        }

        if (søknad instanceof Svangerskapspengesøknad) {
            Svangerskapspengesøknad svangerskapspengesøknad = (Svangerskapspengesøknad) søknad;
            this.termindato = svangerskapspengesøknad.getBarn().termindato;
            if (!CollectionUtils.isEmpty(svangerskapspengesøknad.getBarn().fødselsdatoer)) {
                this.fødselsdato = svangerskapspengesøknad.getBarn().fødselsdatoer.get(0);
            }
            this.tilrettelegging = svangerskapspengesøknad.getTilrettelegging().stream().map(TilretteleggingDto::new)
                    .collect(toList());
        }

        if (søknad.getAnnenForelder() != null) {
            this.annenForelder = new AnnenForelderDto(søknad.getAnnenForelder());
        }

        if (!søknad.getErEndringssøknad()) {
            this.medlemsskap = new MedlemsskapDto(søknad.getInformasjonOmUtenlandsopphold());
            if ((søknad instanceof Foreldrepengesøknad) || (søknad instanceof Svangerskapspengesøknad)) {
                this.opptjening = new OpptjeningDto(søknad.getSøker());
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
            this.land = annenForelder.getBostedsland();

            if (annenForelder.type().equals("norsk")) {
                this.fnr = annenForelder.getFnr();
            } else if (annenForelder.type().equals("utenlandsk")) {
                this.id = annenForelder.getFnr();
            }
        }

        private String navn(AnnenForelder annenForelder) {
            return isNotBlank(annenForelder.getNavn()) ? annenForelder.getNavn()
                    : annenForelder.getFornavn() + " " + annenForelder.getEtternavn();
        }
    }

    @JsonInclude(NON_EMPTY)
    public class MedlemsskapDto {

        public String arbeidSiste12;
        public List<UtenlandsoppholdPeriodeDto> utenlandsopphold = new ArrayList<>();
        public List<UtenlandsoppholdPeriodeDto> framtidigUtenlandsopphold = new ArrayList<>();

        public MedlemsskapDto(Utenlandsopphold opphold) {
            this.arbeidSiste12 = "IKKE_ARBEIDET";

            for (UtenlandsoppholdPeriode tidligere : opphold.getTidligereOpphold()) {
                utenlandsopphold.add(new UtenlandsoppholdPeriodeDto(tidligere));
            }
            for (UtenlandsoppholdPeriode senere : opphold.getSenereOpphold()) {
                framtidigUtenlandsopphold.add(new UtenlandsoppholdPeriodeDto(senere));
            }
        }
    }

    public class UtenlandsoppholdPeriodeDto {

        public String land;
        public PeriodeDto varighet = new PeriodeDto();

        public UtenlandsoppholdPeriodeDto(UtenlandsoppholdPeriode periode) {
            this.land = periode.getLand();
            this.varighet.fom = periode.getTidsperiode().getFom();
            this.varighet.tom = periode.getTidsperiode().getTom();
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
        public LocalDate datoForAleneomsorg;

        public RettigheterDto(Foreldrepengesøknad foreldrepengesøknad) {
            this.harAleneOmsorgForBarnet = foreldrepengesøknad.getSøker().getErAleneOmOmsorg();
            this.harAnnenForelderRett = foreldrepengesøknad.getAnnenForelder().getHarRettPåForeldrepenger();
            this.datoForAleneomsorg = foreldrepengesøknad.getAnnenForelder().getDatoForAleneomsorg();
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
            this.type = tilrettelegging.getType();
            this.arbeidsforhold = new ArbeidsforholdDto(tilrettelegging.getArbeidsforhold());
            this.behovForTilretteleggingFom = tilrettelegging.getBehovForTilretteleggingFom();
            this.tilrettelagtArbeidFom = tilrettelegging.getTilrettelagtArbeidFom();
            this.stillingsprosent = tilrettelegging.getStillingsprosent();
            this.slutteArbeidFom = tilrettelegging.getSlutteArbeidFom();
            this.vedlegg = tilrettelegging.getVedlegg();
        }
    }

    @JsonInclude(NON_NULL)
    public class ArbeidsforholdDto {
        public String type;
        public String orgnr;
        public String fnr;
        public String risikoFaktorer;
        public String tilretteleggingstiltak;

        public ArbeidsforholdDto(Arbeidsforhold arbeidsforhold) {
            this.type = arbeidsforhold.getType();
            this.risikoFaktorer = arbeidsforhold.getRisikofaktorer();
            this.tilretteleggingstiltak = arbeidsforhold.getTilretteleggingstiltak();

            if (arbeidsforhold.getType().equals("virksomhet")) {
                this.orgnr = arbeidsforhold.getId();
            } else {
                this.fnr = arbeidsforhold.getId();
            }

        }
    }
}
