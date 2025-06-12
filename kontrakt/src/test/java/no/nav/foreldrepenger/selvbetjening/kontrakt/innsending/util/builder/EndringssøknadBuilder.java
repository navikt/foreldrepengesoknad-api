package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.SituasjonOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

public class EndringssøknadBuilder {
    private LocalDate mottattdato;
    private SituasjonOLD situasjon;
    private Saksnummer saksnummer;
    private SøkerDtoOLD søker;
    private BarnDtoOLD barn;
    private AnnenforelderDtoOLD annenForelder;
    private String tilleggsopplysninger;
    private Boolean ønskerJustertUttakVedFødsel;
    private List<UttaksplanPeriodeDtoOLD> uttaksplan;
    private List<VedleggDto> vedlegg;

    public EndringssøknadBuilder(Saksnummer saksnummer) {
        this.saksnummer = saksnummer;
    }

    public EndringssøknadBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public EndringssøknadBuilder medSøker(SøkerDtoOLD søker) {
        this.søker = søker;
        return this;
    }

    public EndringssøknadBuilder medBarn(BarnHelper barn) {
        this.situasjon = barn.situasjon();
        this.barn = barn.barn();
        return this;
    }

    public EndringssøknadBuilder medAnnenForelder(AnnenforelderDtoOLD annenforelder) {
        this.annenForelder = annenforelder;
        return this;
    }

    public EndringssøknadBuilder medTilleggsopplysninger(String tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
        return this;
    }

    public EndringssøknadBuilder medØnskerJustertUttakVedFødsel(Boolean ønskerJustertUttakVedFødsel) {
        this.ønskerJustertUttakVedFødsel = ønskerJustertUttakVedFødsel;
        return this;
    }

    public EndringssøknadBuilder medFordeling(List<UttaksplanPeriodeDtoOLD> uttaksplan) {
        this.uttaksplan = uttaksplan;
        return this;
    }

    public EndringssøknadBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public EndringssøknadDtoOLD build() {
        if (mottattdato == null) {
            mottattdato = LocalDate.now();
        }
        return new EndringssøknadForeldrepengerDtoOLD(mottattdato, situasjon, saksnummer, søker, barn, annenForelder, tilleggsopplysninger,
            ønskerJustertUttakVedFødsel, uttaksplan, vedlegg);
    }
}
