package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import java.util.Set;

public record FpSak(Saksnummer saksnummer,
                    boolean sakAvsluttet,
                    boolean kanSøkeOmEndring,
                    boolean sakTilhørerMor,
                    boolean gjelderAdopsjon,
                    boolean morUføretrygd,
                    RettighetType rettighetType,
                    AnnenPart annenPart,
                    Familiehendelse familiehendelse,
                    FpVedtak gjeldendeVedtak,
                    FpÅpenBehandling åpenBehandling,
                    Set<PersonDetaljer> barn,
                    Dekningsgrad dekningsgrad) implements Sak {
}
