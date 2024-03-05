package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.TilretteleggingDto;

public class SvangerskapspengerBuilder {
    LocalDate mottattdato;
    Målform språkkode;
    BarnDto barn;
    UtenlandsoppholdDto utenlandsopphold;
    FrilansInformasjonDto frilansInformasjon;
    List<NæringDto> selvstendigNæringsdrivendeInformasjon;
    List<AnnenInntektDto> andreInntekterSiste10Mnd;
    List<UtenlandsoppholdsperiodeDto> oppholdIUtlandet;
    List<TilretteleggingDto> tilretteleggingsbehov;
    private List<VedleggDto> vedlegg;

    public SvangerskapspengerBuilder(List<TilretteleggingDto> tilretteleggingsbehov) {
        this.tilretteleggingsbehov = tilretteleggingsbehov;
    }

    public SvangerskapspengerBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public SvangerskapspengerBuilder medSpråkkode(Målform språkkode) {
        this.språkkode = språkkode;
        return this;
    }

    public SvangerskapspengerBuilder medBarn(BarnDto barn) {
        this.barn = barn;
        return this;
    }

    public SvangerskapspengerBuilder medFrilansInformasjon(FrilansInformasjonDto frilansInformasjon) {
        this.frilansInformasjon = frilansInformasjon;
        return this;
    }

    public SvangerskapspengerBuilder medSelvstendigNæringsdrivendeInformasjon(List<NæringDto> selvstendigNæringsdrivendeInformasjon) {
        this.selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInformasjon;
        return this;
    }

    public SvangerskapspengerBuilder medAndreInntekterSiste10Mnd(List<AnnenInntektDto> andreInntekterSiste10Mnd) {
        this.andreInntekterSiste10Mnd = andreInntekterSiste10Mnd;
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
            språkkode,
            barn,
            utenlandsopphold,
            frilansInformasjon,
            selvstendigNæringsdrivendeInformasjon,
            andreInntekterSiste10Mnd,
            oppholdIUtlandet,
            tilretteleggingsbehov,
            vedlegg
        );
    }
}
