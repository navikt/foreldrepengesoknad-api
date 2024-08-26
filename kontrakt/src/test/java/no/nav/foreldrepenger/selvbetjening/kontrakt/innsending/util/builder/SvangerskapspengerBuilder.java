package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.AvtaltFerie;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.TilretteleggingDto;

public class SvangerskapspengerBuilder {
    private LocalDate mottattdato;
    private List<TilretteleggingDto> tilrettelegging;

    private List<AvtaltFerie> avtaltFerie = new ArrayList<>();
    private BarnDto barn;
    private UtenlandsoppholdDto informasjonOmUtenlandsopphold;
    private SøkerDto søker;
    private List<VedleggDto> vedlegg;

    public SvangerskapspengerBuilder(List<TilretteleggingDto> tilrettelegging) {
        this.tilrettelegging = tilrettelegging;
    }

    public SvangerskapspengerBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public SvangerskapspengerBuilder medTilrettelegging(List<TilretteleggingDto> tilrettelegging) {
        this.tilrettelegging = tilrettelegging;
        return this;
    }

    public SvangerskapspengerBuilder medBarn(BarnHelper barn) {
        this.barn = barn.barn();
        return this;
    }


    public SvangerskapspengerBuilder medMedlemsskap(UtenlandsoppholdDto informasjonOmUtenlandsopphold) {
        this.informasjonOmUtenlandsopphold = informasjonOmUtenlandsopphold;
        return this;
    }

    public SvangerskapspengerBuilder medSøker(SøkerDto søker) {
        this.søker = søker;
        return this;
    }

    public SvangerskapspengerBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public SvangerskapspengerBuilder medAvtaltFerie(AvtaltFerie avtaltFerie) {
        this.avtaltFerie.add(avtaltFerie);
        return this;
    }

    public SvangerskapspengesøknadDto build() {
        if (mottattdato == null) mottattdato = LocalDate.now();
        return new SvangerskapspengesøknadDto(
                mottattdato,
                tilrettelegging,
                avtaltFerie,
                barn,
                informasjonOmUtenlandsopphold,
                søker,
                vedlegg
        );
    }
}
