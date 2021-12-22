package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import java.util.Set;

record FpÅpenBehandling(BehandlingTilstand tilstand,
                        Set<Søknadsperiode> søknadsperioder) { }
