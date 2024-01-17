package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper.CommonMapper.tilRelasjonTilBarn;

import java.time.LocalDate;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.Ytelse;
import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto;

public final class EngangsstønadMapper {

    private EngangsstønadMapper() {
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
            tilOppholdIUtlandet(e),
            tilRelasjonTilBarn(e.barn())
        );
    }


}
