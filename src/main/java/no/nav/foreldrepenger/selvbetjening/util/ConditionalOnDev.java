package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.LOCAL;

@ConditionalOnClusters(clusters = { DEV_GCP, DEV_SBS, LOCAL })
public @interface ConditionalOnDev {

}
