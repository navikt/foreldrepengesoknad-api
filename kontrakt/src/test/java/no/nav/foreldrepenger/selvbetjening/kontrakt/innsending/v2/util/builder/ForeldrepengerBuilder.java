package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.AnnenForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.Uttaksplanperiode;

public class ForeldrepengerBuilder {
    private LocalDate mottattdato;
    private SøkerDto søker;
    private BarnDto barn;
    private AnnenForelderDto annenForelder;
    private Dekningsgrad dekningsgrad;
    private UtenlandsoppholdDto utenlandsopphold;
    private String tilleggsopplysninger;
    private UttaksplanDto uttaksplan;
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

    public ForeldrepengerBuilder medBarn(BarnDto barn) {
        this.barn = barn;
        return this;
    }

    public ForeldrepengerBuilder medAnnenForelder(AnnenForelderDto annenForelder) {
        this.annenForelder = annenForelder;
        return this;
    }

    public ForeldrepengerBuilder medDekningsgrad(Dekningsgrad dekningsgrad) {
        this.dekningsgrad = dekningsgrad;
        return this;
    }

    public ForeldrepengerBuilder medUtenlandsopphold(UtenlandsoppholdDto utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
        return this;
    }

    public ForeldrepengerBuilder medTilleggsopplysninger(String tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
        return this;
    }

    public ForeldrepengerBuilder medUttaksplan(UttaksplanDto uttaksplan) {
        this.uttaksplan = uttaksplan;
        return this;
    }

    public ForeldrepengerBuilder medUttaksplan(List<Uttaksplanperiode> uttaksperioder) {
        this.uttaksplan = new UttaksplanDto(null, uttaksperioder);
        return this;
    }

    public ForeldrepengerBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public SøknadDto build() {
        if (mottattdato == null) mottattdato = LocalDate.now();
        return new ForeldrepengesøknadDto(
            mottattdato,
            søker,
            barn,
            annenForelder,
            dekningsgrad,
            utenlandsopphold,
            tilleggsopplysninger,
            uttaksplan,
            vedlegg
        );
    }
}
