package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.GCPMellomlagring;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringTjeneste;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;

@Configuration
@ConditionalOnClusters(clusters = { DEV_GCP, PROD_GCP })
public class GCPStorageConfiguration {

    @Bean
    public MellomlagringTjeneste gcpCloudStorage(
            @Value("${storage.mellomlagring.søknad:foreldrepengesoknad}") String søknadBøtte,
            @Value("${storage.mellomlagring.mellomlagring:mellomlagring}") String mellomlagringBøtte,
            @Value("${storage.mellomlagring:enabled:true}") boolean enabled) {
        return new GCPMellomlagring(søknadBøtte, mellomlagringBøtte, enabled);
    }

}
