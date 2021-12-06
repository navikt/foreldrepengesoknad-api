package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import java.util.Set;

record FpÅpenBehandling(BehandlingTilstand tilstand,
                        Set<Søknadsperiode> søknadsperioder) { }
