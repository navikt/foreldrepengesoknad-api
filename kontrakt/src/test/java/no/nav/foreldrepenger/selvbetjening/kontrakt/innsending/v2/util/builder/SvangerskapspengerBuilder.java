package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FrilansDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.TerminDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.AvtaltFerieDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.BarnSvpDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.TilretteleggingbehovDto;

public class SvangerskapspengerBuilder {
    private LocalDate mottattdato;
    private Målform språkkode;
    private BarnSvpDto barn;
    private FrilansDto frilansInformasjon;
    private NæringDto selvstendigNæringsdrivendeInformasjon;
    private List<AnnenInntektDto> andreInntekterSiste10Mnd;
    private List<UtenlandsoppholdsperiodeDto> utenlandsopphold;
    private List<TilretteleggingbehovDto> tilretteleggingbehov;
    private List<AvtaltFerieDto> avtaltFerie;
    private List<VedleggDto> vedlegg;

    public SvangerskapspengerBuilder(List<TilretteleggingbehovDto> tilretteleggingbehov) {
        this.tilretteleggingbehov = tilretteleggingbehov;
        this.språkkode = Målform.standard();
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
        if (barn instanceof FødselDto fødsel) {
            this.barn = new BarnSvpDto(fødsel.termindato(), fødsel.fødselsdato());
        } else if (barn instanceof TerminDto termin) {
            this.barn = new BarnSvpDto(termin.termindato(), null);
        } else {
            throw new IllegalStateException("Svangerskapspengesøknad støtter bare fødsel eller termin!");
        }
        return this;
    }

    public SvangerskapspengerBuilder medFrilansInformasjon(FrilansDto frilansInformasjon) {
        this.frilansInformasjon = frilansInformasjon;
        return this;
    }

    public SvangerskapspengerBuilder medSelvstendigNæringsdrivendeInformasjon(NæringDto selvstendigNæringsdrivendeInformasjon) {
        this.selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInformasjon;
        return this;
    }

    public SvangerskapspengerBuilder medAndreInntekterSiste10Mnd(List<AnnenInntektDto> andreInntekterSiste10Mnd) {
        this.andreInntekterSiste10Mnd = andreInntekterSiste10Mnd;
        return this;
    }

    public SvangerskapspengerBuilder medUtenlandsopphold(List<UtenlandsoppholdsperiodeDto> utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
        return this;
    }

    public SvangerskapspengerBuilder medAvtaltFerie(List<AvtaltFerieDto> avtaltFerie) {
        this.avtaltFerie = avtaltFerie;
        return this;
    }

    public SvangerskapspengerBuilder medVedlegg(List<VedleggDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public SvangerskapspengesøknadDto build() {
        if (mottattdato == null) {
            mottattdato = LocalDate.now();
        }
        return new SvangerskapspengesøknadDto(mottattdato,
            barn,
            BrukerRolle.MOR,
            språkkode,
            frilansInformasjon,
            selvstendigNæringsdrivendeInformasjon,
            andreInntekterSiste10Mnd,
            utenlandsopphold,
            tilretteleggingbehov,
            avtaltFerie,
            vedlegg);
    }
}
