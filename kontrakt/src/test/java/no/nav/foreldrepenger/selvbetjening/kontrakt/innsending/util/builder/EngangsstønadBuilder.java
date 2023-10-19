package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;


public class EngangsstønadBuilder {
    private LocalDate mottattdato;
    private BarnDto barn;
    private UtenlandsoppholdDto informasjonOmUtenlandsopphold;
    private SøkerDto søker;
    private List<VedleggDto> vedlegg;


    public EngangsstønadBuilder() {
    }

    public EngangsstønadBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public EngangsstønadBuilder medBarn(BarnHelper barn) {
        this.barn = barn.barn();
        return this;
    }

    public EngangsstønadBuilder medMedlemsskap(UtenlandsoppholdDto informasjonOmUtenlandsopphold) {
        this.informasjonOmUtenlandsopphold = informasjonOmUtenlandsopphold;
        return this;
    }

    public EngangsstønadBuilder medSøker(SøkerDto søker) {
        this.søker = søker;
        return this;
    }

    public EngangsstønadBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public SøknadDto build() {
        if (mottattdato == null) mottattdato = LocalDate.now();
        return new EngangsstønadDto(
                mottattdato,
                barn,
                informasjonOmUtenlandsopphold,
                søker,
                vedlegg
        );
    }
}
