package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;


public class EngangsstønadBuilder {
    private LocalDate mottattdato;
    private BrukerRolle rolle;
    private Målform språkkode;
    private BarnDto barn;
    private List<UtenlandsoppholdsperiodeDto> utenlandsopphold;
    private List<VedleggDto> vedlegg;

    public EngangsstønadBuilder() {
        this.språkkode = Målform.standard();
    }

    public EngangsstønadBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public EngangsstønadBuilder medSpråkkode(Målform språkkode) {
        this.språkkode = språkkode;
        return this;
    }

    public EngangsstønadBuilder medBrukerRolle(BrukerRolle rolle) {
        this.rolle = rolle;
        return this;
    }

    public EngangsstønadBuilder medBarn(BarnDto barn) {
        this.barn = barn;
        return this;
    }

    public EngangsstønadBuilder medUtenlandsopphold(List<UtenlandsoppholdsperiodeDto> utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
        return this;
    }

    public EngangsstønadBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public EngangsstønadDto build() {
        if (mottattdato == null) {
            mottattdato = LocalDate.now();
        }
        return new EngangsstønadDto(mottattdato, språkkode, rolle, barn, utenlandsopphold, vedlegg);
    }
}
