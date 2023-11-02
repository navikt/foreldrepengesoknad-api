package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.AnnenForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.Uttaksplanperiode;

public class EndringssøknadBuilder {
    private final Saksnummer saksnummer;
    private LocalDate mottattdato;
    private SøkerDto søker;
    private BarnDto barn;
    private AnnenForelderDto annenForelder;
    private String tilleggsopplysninger;
    private UttaksplanDto uttaksplan;
    private List<VedleggDto> vedlegg;

    public EndringssøknadBuilder(Saksnummer saksnummer) {
        this.saksnummer = saksnummer;
    }

    public EndringssøknadBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public EndringssøknadBuilder medSøker(SøkerDto søker) {
        this.søker = søker;
        return this;
    }

    public EndringssøknadBuilder medBarn(BarnDto barn) {
        this.barn = barn;
        return this;
    }

    public EndringssøknadBuilder medAnnenForelder(AnnenForelderDto annenForelder) {
        this.annenForelder = annenForelder;
        return this;
    }

    public EndringssøknadBuilder medTilleggsopplysninger(String tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
        return this;
    }

    public EndringssøknadBuilder medUttaksplan(UttaksplanDto uttaksplan) {
        this.uttaksplan = uttaksplan;
        return this;
    }

    public EndringssøknadBuilder medUttaksplan(List<Uttaksplanperiode> uttaksplanperiodeer) {
        this.uttaksplan = new UttaksplanDto(null, uttaksplanperiodeer);
        return this;
    }

    public EndringssøknadBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public EndringssøknadForeldrepengerDto build() {
        if (mottattdato == null) mottattdato = LocalDate.now();
        return new EndringssøknadForeldrepengerDto(
            mottattdato,
            saksnummer,
            søker,
            barn,
            annenForelder,
            tilleggsopplysninger,
            uttaksplan,
            vedlegg
        );
    }
}
