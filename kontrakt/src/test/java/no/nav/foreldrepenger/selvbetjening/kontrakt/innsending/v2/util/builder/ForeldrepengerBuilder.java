package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FrilansDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.AnnenForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.Uttaksplanperiode;

public class ForeldrepengerBuilder {
    private LocalDate mottattdato;
    private BrukerRolle rolle;
    private Målform språkkode;
    private FrilansDto frilansInformasjon;
    private NæringDto selvstendigNæringsdrivendeInformasjon;
    private List<AnnenInntektDto> andreInntekterSiste10Mnd;
    private BarnDto barn;
    private AnnenForelderDto annenForelder;
    private Dekningsgrad dekningsgrad;
    private List<UtenlandsoppholdsperiodeDto> utenlandsopphold;
    private UttaksplanDto uttaksplan;
    private List<VedleggDto> vedlegg;

    public ForeldrepengerBuilder() {
        this.språkkode = Målform.standard();
    }

    public ForeldrepengerBuilder medMottattdato(LocalDate mottattdato) {
        this.mottattdato = mottattdato;
        return this;
    }

    public ForeldrepengerBuilder medRolle(BrukerRolle rolle) {
        this.rolle = rolle;
        return this;
    }

    public ForeldrepengerBuilder medSpråkkode(Målform språkkode) {
        this.språkkode = språkkode;
        return this;
    }

    public ForeldrepengerBuilder medFrilansInformasjon(FrilansDto frilansInformasjon) {
        this.frilansInformasjon = frilansInformasjon;
        return this;
    }

    public ForeldrepengerBuilder medSelvstendigNæringsdrivendeInformasjon(NæringDto selvstendigNæringsdrivendeInformasjon) {
        this.selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInformasjon;
        return this;
    }

    public ForeldrepengerBuilder medAndreInntekterSiste10Mnd(List<AnnenInntektDto> andreInntekterSiste10Mnd) {
        this.andreInntekterSiste10Mnd = andreInntekterSiste10Mnd;
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

    public ForeldrepengerBuilder medUtenlandsopphold(List<UtenlandsoppholdsperiodeDto> utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
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
        if (mottattdato == null) {
            mottattdato = LocalDate.now();
        }
        return new ForeldrepengesøknadDto(mottattdato,
            rolle,
            språkkode,
            frilansInformasjon,
            selvstendigNæringsdrivendeInformasjon,
            andreInntekterSiste10Mnd,
            barn,
            annenForelder,
            dekningsgrad,
            uttaksplan,
            utenlandsopphold,
            vedlegg);
    }
}
