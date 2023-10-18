package no.nav.foreldrepenger.selvbetjening.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ImageVersionContributor implements InfoContributor {

    @Autowired
    Environment env;

    @Override
    public void contribute(Builder builder) {
        Map<String, String> extras = new HashMap<>();
        extras.put("cluster", env.getProperty("nais.cluster.name"));
        extras.put("image version", env.getProperty("nais.app.image"));
        builder.withDetail("extras", extras);
    }
}
