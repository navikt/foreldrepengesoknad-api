package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.AnnenForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.Uttaksplanperiode;

public class EndringssøknadBuilder {
    private final Saksnummer saksnummer;
    private BrukerRolle rolle;
    private Målform språkkode;
    private LocalDate mottattdato;
    private BarnDto barn;
    private AnnenForelderDto annenForelder;
    private UttaksplanDto uttaksplan;
    private List<VedleggDto> vedlegg;

    public EndringssøknadBuilder(Saksnummer saksnummer) {
        this.saksnummer = saksnummer;
        this.språkkode = Målform.standard();
    }

    public EndringssøknadBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public EndringssøknadBuilder medRolle(BrukerRolle rolle) {
        this.rolle = rolle;
        return this;
    }

    public EndringssøknadBuilder medSpråkkode(Målform språkkode) {
        this.språkkode = språkkode;
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
        if (mottattdato == null) {
            mottattdato = LocalDate.now();
        }
        return new EndringssøknadForeldrepengerDto(mottattdato, rolle, språkkode, barn, annenForelder, uttaksplan, saksnummer, vedlegg);
    }
}
