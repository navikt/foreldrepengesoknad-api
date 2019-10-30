package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_SBS;

@ConditionalOnClusters(clusters = { DEV_GCP, PROD_GCP, DEV_SBS, PROD_SBS })
public @interface ConditionalOnK8s {

}
