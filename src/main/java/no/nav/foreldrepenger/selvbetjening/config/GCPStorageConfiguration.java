package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.GCPCloudStorage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;

@Configuration
@ConditionalOnClusters(clusters = { DEV_GCP, PROD_GCP })
public class GCPStorageConfiguration {

    @Bean
    public Storage gcpCloudStorage(@Value("${storage.søknad:foreldrepengesoknad}") String søknadBucket,
            @Value("${storage.mellomlagring:mellomlagring}") String mellomlagringBucket) {
        return new GCPCloudStorage(søknadBucket, mellomlagringBucket);
    }

}
