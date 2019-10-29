package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.LOCAL;

@ConditionalOnClusters(clusters = LOCAL)
public @interface ConditionalOnLocal {

}
