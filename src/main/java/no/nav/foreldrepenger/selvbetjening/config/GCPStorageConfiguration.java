package no.nav.foreldrepenger.selvbetjening.config;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.SØKNAD;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte.TMP;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.GCPMellomlagring;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Mellomlagring;
import no.nav.foreldrepenger.selvbetjening.util.conditionals.ConditionalOnGCP;

@Configuration
@ConditionalOnGCP
public class GCPStorageConfiguration {

    @Bean
    public Mellomlagring gcpMellomlagring(
            @Qualifier(SØKNAD) Bøtte søknad,
            @Qualifier(TMP) Bøtte mellomlagring) {
        return new GCPMellomlagring(søknad, mellomlagring);
    }
}
