package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.EqualsAndHashCode;
import no.nav.foreldrepenger.common.domain.Saksnummer;

@EqualsAndHashCode
@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
        @Type(value = EngangsstønadFrontend.class, name = "engangsstønad"),
        @Type(value = ForeldrepengesøknadFrontend.class, name = "foreldrepenger"),
        @Type(value = SvangerskapspengesøknadFrontend.class, name = "svangerskapspenger")
})
public abstract sealed class SøknadFrontend permits EngangsstønadFrontend, ForeldrepengesøknadFrontend, SvangerskapspengesøknadFrontend {

    private LocalDateTime opprettet;
    @Pattern(regexp = BARE_BOKSTAVER)
    private final String type;
    @Valid
    private final Saksnummer saksnummer;
    @Valid
    private final SøkerFrontend søker;
    @Valid
    private final BarnFrontend barn;
    @Valid
    private final AnnenForelderFrontend annenForelder;
    @Valid
    private final UtenlandsoppholdFrontend informasjonOmUtenlandsopphold;
    @Pattern(regexp = BARE_BOKSTAVER)
    private final String situasjon;
    private final Boolean erEndringssøknad;
    @Pattern(regexp = FRITEKST)
    private final String tilleggsopplysninger;
    @Valid
    @Size(max = 40)
    private final List<VedleggFrontend> vedlegg;

    @JsonCreator
    protected SøknadFrontend(LocalDateTime opprettet, String type, Saksnummer saksnummer, SøkerFrontend søker, BarnFrontend barn,
                             AnnenForelderFrontend annenForelder, UtenlandsoppholdFrontend informasjonOmUtenlandsopphold, String situasjon,
                             Boolean erEndringssøknad, String tilleggsopplysninger, List<VedleggFrontend> vedlegg) {
        this.opprettet = opprettet;
        this.type = type;
        this.saksnummer = saksnummer;
        this.søker = søker;
        this.barn = barn;
        this.annenForelder = annenForelder;
        this.informasjonOmUtenlandsopphold = informasjonOmUtenlandsopphold;
        this.situasjon = situasjon;
        this.erEndringssøknad = erEndringssøknad;
        this.tilleggsopplysninger = tilleggsopplysninger;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }

    public String getType() {
        return type;
    }

    public Saksnummer getSaksnummer() {
        return saksnummer;
    }

    public SøkerFrontend getSøker() {
        return søker;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public BarnFrontend getBarn() {
        return barn;
    }

    public AnnenForelderFrontend getAnnenForelder() {
        return annenForelder;
    }

    public UtenlandsoppholdFrontend getInformasjonOmUtenlandsopphold() {
        return informasjonOmUtenlandsopphold;
    }

    public String getSituasjon() {
        return situasjon;
    }

    public Boolean getErEndringssøknad() {
        return erEndringssøknad;
    }

    public String getTilleggsopplysninger() {
        return tilleggsopplysninger;
    }

    public List<VedleggFrontend> getVedlegg() {
        return vedlegg;
    }

    @Override
    public String toString() {
        return "Søknad{" +
            "type='" + type + '\'' +
            ", saksnummer='" + saksnummer + '\'' +
            ", søker=" + søker +
            ", opprettet=" + opprettet +
            ", barn=" + barn +
            ", annenForelder=" + annenForelder +
            ", informasjonOmUtenlandsopphold=" + informasjonOmUtenlandsopphold +
            ", situasjon='" + situasjon + '\'' +
            ", erEndringssøknad=" + erEndringssøknad +
            ", tilleggsopplysninger='" + tilleggsopplysninger + '\'' +
            ", vedlegg=" + vedlegg +
            '}';
    }
}
