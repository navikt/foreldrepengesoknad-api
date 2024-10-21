package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDto;

public class ForeldrepengerBuilder {
    private LocalDate mottattdato;
    private Situasjon situasjon;
    private SøkerDto søker;
    private AnnenforelderDto annenForelder;
    private BarnDto barn;
    private Dekningsgrad dekningsgrad;
    private String tilleggsopplysninger;
    private UtenlandsoppholdDto informasjonOmUtenlandsopphold;
    private List<UttaksplanPeriodeDto> uttaksplan;
    private Boolean ønskerJustertUttakVedFødsel;
    private List<VedleggDto> vedlegg;

    public ForeldrepengerBuilder() {

    }

    public ForeldrepengerBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public ForeldrepengerBuilder medSøker(SøkerDto søker) {
        this.søker = søker;
        return this;
    }

    public ForeldrepengerBuilder medBarn(BarnHelper barn) {
        this.situasjon = barn.situasjon();
        this.barn = barn.barn();
        return this;
    }

    public ForeldrepengerBuilder medAnnenForelder(AnnenforelderDto annenForelder) {
        this.annenForelder = annenForelder;
        return this;
    }

    public ForeldrepengerBuilder medDekningsgrad(Dekningsgrad dekningsgrad) {
        this.dekningsgrad = dekningsgrad;
        return this;
    }

    public ForeldrepengerBuilder medTilleggsopplysninger(String tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
        return this;
    }

    public ForeldrepengerBuilder medMedlemsskap(UtenlandsoppholdDto informasjonOmUtenlandsopphold) {
        this.informasjonOmUtenlandsopphold = informasjonOmUtenlandsopphold;
        return this;
    }

    public ForeldrepengerBuilder medFordeling(List<UttaksplanPeriodeDto> uttaksplan) {
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

    public SøknadDto build() {
        if (mottattdato == null) mottattdato = LocalDate.now();
        return new ForeldrepengesøknadDto(
                this.mottattdato,
                this.situasjon,
                this.søker,
                this.barn,
                this.annenForelder,
                this.dekningsgrad,
                this.tilleggsopplysninger,
                this.informasjonOmUtenlandsopphold,
                this.uttaksplan,
                this.ønskerJustertUttakVedFødsel,
                this.vedlegg
        );
    }
}
