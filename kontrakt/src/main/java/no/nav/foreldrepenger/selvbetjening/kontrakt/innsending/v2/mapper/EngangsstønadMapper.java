package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.Ytelse;
import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;

import java.time.LocalDate;
import java.util.List;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilRelasjonTilBarn;

public final class EngangsstønadMapper {

    private EngangsstønadMapper() {
    }

    public static Søknad tilEngangsstønad(EngangsstønadDto e, List<VedleggDto> påkrevdeVedlegg, LocalDate mottattDato) {
        return new Søknad(mottattDato, tilSøker(e.språkkode()), tilYtelse(e, påkrevdeVedlegg), null,
                          tilVedlegg(påkrevdeVedlegg));
    }

    private static Søker tilSøker(Målform språkkode) {
        return new Søker(BrukerRolle.MOR,
                         språkkode); // TODO: Frontend sender ikke ned søker her. Kan også være Far/Medmor!
    }

    private static Ytelse tilYtelse(EngangsstønadDto e, List<VedleggDto> vedlegg) {
        return new Engangsstønad(tilOppholdIUtlandet(e.utenlandsopphold()), tilRelasjonTilBarn(e.barn(), vedlegg));
    }


}
