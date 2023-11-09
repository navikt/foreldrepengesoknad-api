package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.Ytelse;
import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Medlemsskap;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Utenlandsopphold;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Omsorgsovertakelse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.RelasjonTilBarn;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AdopsjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.OmsorgsovertakelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.TerminDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdDto;

public final class EngangsstønadMapperV2 {

    private EngangsstønadMapperV2() {
    }

    public static Søknad tilEngangsstønad(EngangsstønadDto e, LocalDate mottattDato) {
        return new Søknad(
            mottattDato,
            tilSøker(e.språkkode()),
            tilYtelse(e),
            null,
            tilVedlegg(e.vedlegg())
        );
    }

    private static Søker tilSøker(Målform språkkode) {
        return new Søker(BrukerRolle.MOR, språkkode); // TODO: Frontend sender ikke ned søker her. Kan også være Far/Medmor!
    }

    private static Ytelse tilYtelse(EngangsstønadDto e) {
        return new Engangsstønad(
            tilMedlemskap(e.utenlandsopphold()),
            tilRelasjonTilBarn(e.barn())
        );
    }

    static Medlemsskap tilMedlemskap(UtenlandsoppholdDto utenlandsopphol) {
        return new Medlemsskap(
            tilUtenlandsoppholdsliste(utenlandsopphol.utenlandsoppholdSiste12Mnd()),
            tilUtenlandsoppholdsliste(utenlandsopphol.utenlandsoppholdNeste12Mnd()));
    }

    private static List<Utenlandsopphold> tilUtenlandsoppholdsliste(List<UtenlandsoppholdDto.Periode> opphold) {
        return safeStream(opphold)
            .map(EngangsstønadMapperV2::tilUtenlandsopphold)
            .toList();
    }

    private static Utenlandsopphold tilUtenlandsopphold(UtenlandsoppholdDto.Periode o) {
        return new Utenlandsopphold(o.landkode(), new LukketPeriode(o.fom(), o.tom()));
    }

    static RelasjonTilBarn tilRelasjonTilBarn(BarnDto barn) {
        if (barn instanceof FødselDto f) {
            return tilFødsel(f);
        } else if (barn instanceof TerminDto t) {
            return tilFremtidigFødsel(t);
        } else if (barn instanceof AdopsjonDto a) {
            return tilAdopsjon(a);
        } else if (barn instanceof OmsorgsovertakelseDto o) {
            return tilOmsorgsovertagelse(o);
        } else {
            throw new IllegalStateException("Utviklerfeil: Skal ikke være mulig med annen type barn enn fødsel, termin, adopsjon eller omsorgsovertakelse");
        }
    }

    static Fødsel tilFødsel(FødselDto barn) {
        return new Fødsel(
            barn.antallBarn(),
            List.of(barn.fødselsdato()), // TODO: Fjern liste i mottak!
            barn.termindato(),
            tilVedleggsreferanse(barn.vedleggreferanser())
        );
    }

    private static FremtidigFødsel tilFremtidigFødsel(TerminDto barn) {
        return new FremtidigFødsel(
            barn.antallBarn(),
            barn.termindato(),
            barn.terminbekreftelseDato(),
            tilVedleggsreferanse(barn.vedleggreferanser())
        );
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(OmsorgsovertakelseDto barn) {
        return new Omsorgsovertakelse(
            barn.antallBarn(),
            barn.foreldreansvarsdato(),
            barn.fødselsdatoer(),
            tilVedleggsreferanse(barn.vedleggreferanser())
        );
    }

    private static Adopsjon tilAdopsjon(AdopsjonDto barn) {
        return new Adopsjon(
            barn.antallBarn(),
            barn.adopsjonsdato(),
            barn.adopsjonAvEktefellesBarn(),
            barn.søkerAdopsjonAlene() != null && barn.søkerAdopsjonAlene(),
            tilVedleggsreferanse(barn.vedleggreferanser()),
            barn.ankomstdato(),
            barn.fødselsdatoer()
        );
    }
}
