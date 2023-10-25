package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadV2Dto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.UtenlandsoppholdDto;


public class EngangsstønadV2Builder {
    private LocalDate mottattdato;
    private Målform språkkode;
    private BarnDto barn;
    private UtenlandsoppholdDto utenlandsopphold;
    private List<VedleggDto> vedlegg;


    public EngangsstønadV2Builder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public EngangsstønadV2Builder medSpråkkode(Målform språkkode) {
        this.språkkode = språkkode;
        return this;
    }

    public EngangsstønadV2Builder medBarn(BarnDto barn) {
        this.barn = barn;
        return this;
    }

    public EngangsstønadV2Builder medUtenlandsopphold(UtenlandsoppholdDto utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
        return this;
    }

    public EngangsstønadV2Builder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public EngangsstønadV2Dto build() {
        if (mottattdato == null) mottattdato = LocalDate.now();
        return new EngangsstønadV2Dto(
            mottattdato,
            språkkode,
            barn,
            utenlandsopphold,
            vedlegg
        );
    }
}
