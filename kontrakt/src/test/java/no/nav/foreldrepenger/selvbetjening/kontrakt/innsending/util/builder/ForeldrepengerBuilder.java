package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.DekningsgradOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.SituasjonOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

public class ForeldrepengerBuilder {
    private LocalDate mottattdato;
    private SituasjonOLD situasjon;
    private SøkerDtoOLD søker;
    private AnnenforelderDtoOLD annenForelder;
    private BarnDtoOLD barn;
    private DekningsgradOLD dekningsgrad;
    private String tilleggsopplysninger;
    private UtenlandsoppholdDtoOLD informasjonOmUtenlandsopphold;
    private List<UttaksplanPeriodeDtoOLD> uttaksplan;
    private Boolean ønskerJustertUttakVedFødsel;
    private List<VedleggDto> vedlegg;

    public ForeldrepengerBuilder() {

    }

    public ForeldrepengerBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public ForeldrepengerBuilder medSøker(SøkerDtoOLD søker) {
        this.søker = søker;
        return this;
    }

    public ForeldrepengerBuilder medBarn(BarnHelper barn) {
        this.situasjon = barn.situasjon();
        this.barn = barn.barn();
        return this;
    }

    public ForeldrepengerBuilder medAnnenForelder(AnnenforelderDtoOLD annenForelder) {
        this.annenForelder = annenForelder;
        return this;
    }

    public ForeldrepengerBuilder medDekningsgrad(DekningsgradOLD dekningsgrad) {
        this.dekningsgrad = dekningsgrad;
        return this;
    }

    public ForeldrepengerBuilder medTilleggsopplysninger(String tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
        return this;
    }

    public ForeldrepengerBuilder medMedlemsskap(UtenlandsoppholdDtoOLD informasjonOmUtenlandsopphold) {
        this.informasjonOmUtenlandsopphold = informasjonOmUtenlandsopphold;
        return this;
    }

    public ForeldrepengerBuilder medFordeling(List<UttaksplanPeriodeDtoOLD> uttaksplan) {
        this.uttaksplan = uttaksplan;
        return this;
    }

    public ForeldrepengerBuilder medØnskerJustertUttakVedFødsel(Boolean ønskerJustertUttakVedFødsel) {
        this.ønskerJustertUttakVedFødsel = ønskerJustertUttakVedFødsel;
        return this;
    }

    public ForeldrepengerBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public SøknadDtoOLD build() {
        if (mottattdato == null) {
            mottattdato = LocalDate.now();
        }
        return new ForeldrepengesøknadDtoOLD(this.mottattdato, this.situasjon, this.søker, this.barn, this.annenForelder, this.dekningsgrad,
            this.tilleggsopplysninger, this.informasjonOmUtenlandsopphold, this.uttaksplan, this.ønskerJustertUttakVedFødsel, this.vedlegg);
    }
}
