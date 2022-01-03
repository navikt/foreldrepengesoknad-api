package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import java.util.Set;

record FpSak(Saksnummer saksnummer,
             boolean sakAvsluttet,
             boolean kanSøkeOmEndring,
             boolean sakTilhørerMor,
             boolean gjelderAdopsjon,
             RettighetType rettighetType,
             AnnenPart annenPart,
             Familiehendelse familiehendelse,
             FpVedtak gjeldendeVedtak,
             FpÅpenBehandling åpenBehandling,
             Set<AktørId> barn,
             Dekningsgrad dekningsgrad) implements Sak {
}
