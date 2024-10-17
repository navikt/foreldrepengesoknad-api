package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;

public interface SøknadDto extends Innsending {
    BrukerRolle rolle();
    Målform språkkode();
    List<UtenlandsoppholdsperiodeDto> utenlandsopphold();
    List<VedleggDto> vedlegg();

    default String navn() {
        if (this instanceof ForeldrepengesøknadDto) return "foreldrepenger";
        if (this instanceof EngangsstønadDto) return "engangsstønad";
        if (this instanceof SvangerskapspengesøknadDto) return "svangersskapspenger";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp, svp eller es!");
    }
}
