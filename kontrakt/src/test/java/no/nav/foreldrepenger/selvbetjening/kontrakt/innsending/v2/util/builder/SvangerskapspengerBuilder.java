package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.TilretteleggingDto;

public class SvangerskapspengerBuilder {
    private LocalDate mottattdato;
    private BarnDto barn;
    private SøkerDto søker;
    private UtenlandsoppholdDto utenlandsopphold;
    private List<UtenlandsoppholdsperiodeDto> oppholdIUtlandet;
    private final List<TilretteleggingDto> tilrettelegging;
    private List<VedleggDto> vedlegg;

    public SvangerskapspengerBuilder(List<TilretteleggingDto> tilrettelegging) {
        this.tilrettelegging = tilrettelegging;
    }

    public SvangerskapspengerBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public SvangerskapspengerBuilder medBarn(BarnDto barn) {
        this.barn = barn;
        return this;
    }

    public SvangerskapspengerBuilder medSøker(SøkerDto søker) {
        this.søker = søker;
        return this;
    }

    public SvangerskapspengerBuilder medOppholdIUtlandet(List<UtenlandsoppholdsperiodeDto> oppholdIUtlandet) {
        this.oppholdIUtlandet = oppholdIUtlandet;
        return this;
    }

    public SvangerskapspengerBuilder medUtenlandsopphold(UtenlandsoppholdDto utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
        return this;
    }

    public SvangerskapspengerBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public SvangerskapspengesøknadDto build() {
        if (mottattdato == null) mottattdato = LocalDate.now();
        return new SvangerskapspengesøknadDto(
            mottattdato,
            barn,
            søker,
            utenlandsopphold,
            oppholdIUtlandet,
            tilrettelegging,
            vedlegg
        );
    }
}
