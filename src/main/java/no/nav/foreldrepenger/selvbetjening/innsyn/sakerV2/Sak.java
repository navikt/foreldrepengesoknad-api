package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import java.util.Set;

interface Sak {

    Saksnummer saksnummer();

    Familiehendelse familiehendelse();

    Set<AktørId> barn();

    boolean gjelderAdopsjon();

    boolean sakAvsluttet();

}
