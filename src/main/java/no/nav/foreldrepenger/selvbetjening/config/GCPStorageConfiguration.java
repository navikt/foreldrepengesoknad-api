package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.GCPMellomlagring;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Mellomlagring;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;

@Configuration
@ConditionalOnClusters(clusters = { DEV_GCP, PROD_GCP })
public class GCPStorageConfiguration {

    @Bean
    public Mellomlagring gcpCloudStorage(
            @Qualifier(Bøtte.SØKNAD) Bøtte søknadBøtte,
            @Qualifier(Bøtte.TMP) Bøtte mellomlagringBøtte) {
        return new GCPMellomlagring(søknadBøtte, mellomlagringBøtte);
    }

}
