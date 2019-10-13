package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD;

@ConditionalOnClusters(clusters = { DEV, PROD })
public @interface ConditionalOnPremise {

}
