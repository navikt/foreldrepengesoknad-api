package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import java.util.Set;

record SvpSak(Saksnummer saksnummer,
              boolean sakAvsluttet,
              Familiehendelse familiehendelse,
              Set<AktørId> barn) implements Sak {
}
