package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import java.util.Set;

interface Sak {

    Saksnummer saksnummer();

    Familiehendelse familiehendelse();

    Set<PersonDetaljer> barn();

    boolean gjelderAdopsjon();

    boolean sakAvsluttet();

}
