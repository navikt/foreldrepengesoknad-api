package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;

@ConditionalOnClusters(clusters = { DEV_GCP, PROD_GCP })
public @interface ConditionalOnGCP {

}
